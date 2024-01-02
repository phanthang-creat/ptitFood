package com.server.ptitFood.domain.services;

import com.server.ptitFood.domain.dto.ProducerDto;
import com.server.ptitFood.domain.entities.Admin;
import com.server.ptitFood.domain.entities.Producer;
import com.server.ptitFood.domain.repositories.ProducerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class ProducerService {
    final private ProducerRepository producerRepository;

    final private AdminControlService adminControlService;

    public ProducerService(ProducerRepository producerRepository, AdminControlService adminControlService) {
        this.producerRepository = producerRepository;
        this.adminControlService = adminControlService;
    }

    public Producer selectProductDecryptionById(Integer id) {
        if (id == null) return null;
        return producerRepository.findById(Long.valueOf(id)).isPresent() ? producerRepository.findById(Long.valueOf(id)).get() : null;
    }

    public boolean insertProducer(ProducerDto dto) {
        try {
            Admin id = adminControlService.getAdminByUserName();
            Producer producer = new Producer();
            producer.setName(dto.getName());
            producer.setCode(dto.getCode());
            producer.setKeyword(dto.getKeyword());
            producer.setCreated(new Date(System.currentTimeMillis()));
            producer.setUpdated(new Date(System.currentTimeMillis()));
            producer.setCreatedBy(id);
            producer.setUpdatedBy(id);
            producer.setStatus(dto.getStatus());
            producerRepository.save(producer);

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public Page<Producer> findAll(Pageable pageable) {
        List<Producer> producers = producerRepository.findAll();
        return new PageImpl<>(producers, pageable, producers.size());
    }

    public Producer findById(Integer id) {
        return producerRepository.findById(Long.valueOf(id)).isPresent() ? producerRepository.findById(Long.valueOf(id)).get() : null;
    }

    public void updateProducerById(ProducerDto dto) {
        Admin id = adminControlService.getAdminByUserName();

        Producer producer = findById(dto.getId());
        producer.setName(dto.getName());
        producer.setCode(dto.getCode());
        producer.setKeyword(dto.getKeyword());
        producer.setStatus(dto.getStatus());
        producer.setUpdatedBy(id);
        producerRepository.save(producer);
    }

    public void deleteProducerById(Integer id) {
        producerRepository.deleteById(id);
    }

    public List<Producer> findAll() {
        return producerRepository.findAll();
    }
}
