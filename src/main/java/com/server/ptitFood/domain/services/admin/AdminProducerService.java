package com.server.ptitFood.domain.services.admin;

import com.server.ptitFood.domain.dto.ProducerDto;
import com.server.ptitFood.domain.entities.Producer;
import com.server.ptitFood.domain.repositories.ProducerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminProducerService {
    final private ProducerRepository producerRepository;

    final private AdminControlService adminControlService;

    public AdminProducerService(ProducerRepository producerRepository, AdminControlService adminControlService) {
        this.producerRepository = producerRepository;
        this.adminControlService = adminControlService;
    }

    public void selectProductDecryption() {

    }

    public void insertProducer(ProducerDto dto) {
        Integer id = adminControlService.getAdminByUserName().getId();

        producerRepository.insertProducer(
                dto.getName(),
                dto.getCode(),
                dto.getKeywork(),
                id,
                id,
                dto.getStatus(),
                0
        );
    }

    public Page<Producer> findAll(Pageable pageable) {
        List<Producer> producers = producerRepository.selectProducerDecryption();
        return new PageImpl<>(producers, pageable, producers.size());
    }

    public Producer findById(Integer id) {
        return producerRepository.selectProducerDecryptionById(id);
    }

    public void updateProducerById(ProducerDto dto) {
        Integer id = adminControlService.getAdminByUserName().getId();

        producerRepository.updateProducerById(
                dto.getId(),
                dto.getName(),
                dto.getCode(),
                dto.getKeywork(),
                id,
                dto.getStatus()
        );
    }

    public void deleteProducerById(Integer id) {
        producerRepository.deleteById(id);
    }

    public List<Producer> findAll() {
        return producerRepository.selectProducerDecryption();
    }
}
