package capstone.skini.domain.diagnosis.service;

import capstone.skini.aws.service.AwsS3Service;
import capstone.skini.domain.diagnosis.dto.DiagnosisDto;
import capstone.skini.domain.diagnosis.dto.DiagnosisFastApiDto;
import capstone.skini.domain.diagnosis.entity.Diagnosis;
import capstone.skini.domain.diagnosis.entity.DiagnosisType;
import capstone.skini.domain.diagnosis.repository.DiagnosisRepository;
import capstone.skini.domain.user.entity.User;
import capstone.skini.domain.user.service.UserService;
import com.amazonaws.services.s3.AmazonS3Client;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final UserService userService;
    private final RestTemplate restTemplate;
    private final AwsS3Service awsS3Service;

    @Transactional(readOnly = true)
    public Diagnosis findById(Long id) {
        return diagnosisRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot Find Diagnosis By ID : " + id));
    }

    @Transactional(readOnly = true)
    public List<Diagnosis> findAll() {
        return diagnosisRepository.findAll();
    }


    public ResponseEntity<?> findDiagnosis(String loginId) {
        User findUser = userService.findByLoginId(loginId);
        if (findUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("cannot find User By loginId : " + loginId);
        }
        List<Diagnosis> allByUser = diagnosisRepository.findAllByUser(findUser);
        List<DiagnosisDto> diagnosisDtos = allByUser.stream().map(diagnosis -> new DiagnosisDto(diagnosis)).collect(Collectors.toList());
        return ResponseEntity.ok(diagnosisDtos);
    }

    public ResponseEntity<?> deleteDiagnosis(Long id, String loginId) {
        try {
            User findUser = userService.findByLoginId(loginId);
            if (findUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("cannot find User By loginId : " + loginId);
            }
            Diagnosis diagnosis = diagnosisRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("cannot find Diagnosis By Id: " + id));
            if (diagnosis.getUser().getId() != findUser.getId()) {
                return ResponseEntity.badRequest().body("진단기록의 유저 id와 현재 유저의 id가 다릅니다.");
            }
            diagnosisRepository.delete(diagnosis);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public ResponseEntity<?> diagnose(String loginId, DiagnosisType diagnosisType, MultipartFile file) {
        try {
            String fastApiUrl;
            String cancerOrDisease;
            if (diagnosisType == DiagnosisType.DISEASE) {
                fastApiUrl = "http://13.125.74.150:8080/predict";
                cancerOrDisease = "질병여부";
            } else if (diagnosisType == DiagnosisType.CANCER) {
                fastApiUrl = "http://13.125.74.150:8081/predict";
                cancerOrDisease = "암여부";
            } else {
                return ResponseEntity.badRequest().body("잘못된 진단타입입니다 : " + diagnosisType);
            }

            ResponseEntity<Map> response = requestFastApi(file, fastApiUrl);
            Map<String, Object> responseBody = response.getBody();
            boolean isPositive = (boolean) responseBody.get(cancerOrDisease);
            String result = (String) responseBody.get("질병명");
            String confidenceScore = (String) responseBody.get("확률");

            User findUser = null;
            if (loginId != null) {
                findUser = userService.findByLoginId(loginId);
                if (findUser == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("cannot find User By loginId : " + loginId);
                }
            }
            //AWS S3에 이미지 업로드
            String imageUrl = awsS3Service.upload(file, diagnosisType.toString());

            Diagnosis newDiagnosis = Diagnosis.builder()
                    .diagnosisType(diagnosisType)
                    .isPositive(isPositive)
                    .result(result)
                    .confidenceScore(confidenceScore)
                    .imageUrl(imageUrl)
                    .user(findUser)
                    .build();
            Diagnosis savedDiagnosis = diagnosisRepository.save(newDiagnosis);

            // 클라이언트에 FAST API 응답 반환
            return ResponseEntity.ok(new DiagnosisFastApiDto(savedDiagnosis));

        } catch (RestClientException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("FAST api 서버와의 통신에 문제가 발생하였습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("이미지의 getBytes() 메서드에서 오류가 발생하였습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * 머신러닝 FAST api 요청
     */
    private ResponseEntity<Map> requestFastApi(MultipartFile file, String fastApiUrl) throws IOException {
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 요청 본문에 ByteArrayResource 추가
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename(); // 파일 이름 설정
            }
        });

        // HTTP 요청 생성
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // POST 요청 보내기
        ResponseEntity<Map> response = restTemplate.exchange(
                fastApiUrl,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        return response;
    }
}
