package com.example.capstone.controller;

import com.example.capstone.dto.ResponseOutputDTO;
import com.example.capstone.service.VolumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class VolumeController {

    private VolumeService volumeService;

    @Autowired
    public VolumeController(VolumeService volumeService) {
        this.volumeService = volumeService;
    }

    @GetMapping("/volume-rank")
    public Mono<List<ResponseOutputDTO>> getVolumeRank() {
        return volumeService.getVolumeRank();
    }

}
