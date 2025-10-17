package com.cafeteria.gestao_cafeteria.controller;

import com.cafeteria.gestao_cafeteria.dto.MesaCreateDTO;
import com.cafeteria.gestao_cafeteria.dto.MesaDTO;
// MUDANÇA: A entidade Mesa não é mais importada, pois nunca será retornada.
// import com.cafeteria.gestao_cafeteria.model.Mesa;
import com.cafeteria.gestao_cafeteria.service.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    @PostMapping
    // A assinatura já estava correta, mas o corpo foi otimizado.
    public ResponseEntity<MesaDTO> criarMesa(@RequestBody MesaCreateDTO mesaDTO) {
        // Assumindo que o service.criarMesa agora retorna o DTO diretamente.
        MesaDTO novaMesaDto = mesaService.criarMesa(mesaDTO);
        return new ResponseEntity<>(novaMesaDto, HttpStatus.CREATED);
    }

    @GetMapping
    // MUDANÇA: A assinatura agora retorna uma lista de DTOs.
    public ResponseEntity<List<MesaDTO>> listarMesas(@RequestParam(required = false) Boolean ativo) {
        // MUDANÇA: A lógica foi movida para o MesaService.
        List<MesaDTO> mesas = mesaService.listarMesas(ativo);
        return ResponseEntity.ok(mesas);
    }

    @GetMapping("/{id}")
    // Endpoint para buscar uma única mesa por ID, que estava faltando.
    public ResponseEntity<MesaDTO> buscarMesaPorId(@PathVariable Long id) {
        MesaDTO mesa = mesaService.buscarPorId(id);
        return ResponseEntity.ok(mesa);
    }

    @PutMapping("/{id}")
    // MUDANÇA: A assinatura agora retorna um DTO.
    public ResponseEntity<MesaDTO> atualizarMesa(@PathVariable Long id, @RequestBody MesaCreateDTO mesaDTO) {
        // O service.atualizarMesa deve ser ajustado para retornar o DTO.
        MesaDTO mesaAtualizada = mesaService.atualizarMesa(id, mesaDTO);
        return ResponseEntity.ok(mesaAtualizada);
    }

    @PatchMapping("/{id}/ativar")
    // MUDANÇA: A assinatura agora retorna um DTO.
    public ResponseEntity<MesaDTO> ativarMesa(@PathVariable Long id) {
        // O service.alterarStatusAtivo deve retornar o DTO.
        MesaDTO mesa = mesaService.alterarStatusAtivo(id, true);
        return ResponseEntity.ok(mesa);
    }

    @PatchMapping("/{id}/desativar")
    // MUDANÇA: A assinatura agora retorna um DTO.
    public ResponseEntity<MesaDTO> desativarMesa(@PathVariable Long id) {
        // O service.alterarStatusAtivo deve retornar o DTO.
        MesaDTO mesa = mesaService.alterarStatusAtivo(id, false);
        return ResponseEntity.ok(mesa);
    }

    @DeleteMapping("/{id}")
    // Este método já estava correto.
    public ResponseEntity<Void> deletarMesa(@PathVariable Long id) {
        mesaService.deletarMesa(id);
        return ResponseEntity.noContent().build();
    }
}