package capstone.skini.domain.hospital.service;

import capstone.skini.domain.hospital.entity.Hospital;
import capstone.skini.domain.hospital.repository.HospitalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    @Transactional(readOnly = true)
    public Hospital findById(Long id) {
        return hospitalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot Find Hospital By ID : " + id));
    }

    @Transactional(readOnly = true)
    public List<Hospital> findAll() {
        return hospitalRepository.findAll();
    }

    public Hospital createHospital(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    public void deleteHospital(Hospital hospital) {
        hospitalRepository.delete(hospital);
    }
}
