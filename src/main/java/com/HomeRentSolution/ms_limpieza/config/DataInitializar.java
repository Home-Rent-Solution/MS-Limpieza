package com.HomeRentSolution.ms_limpieza.config;

import com.HomeRentSolution.ms_limpieza.model.EstadoLimpieza;
import com.HomeRentSolution.ms_limpieza.model.Limpieza;
import com.HomeRentSolution.ms_limpieza.repository.LimpiezaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializar implements CommandLineRunner {

    private final LimpiezaRepository limpiezaRepository;

    @Override
    public void run(String... args) {
        if (limpiezaRepository.count() == 0) {
            Limpieza lmp = new Limpieza();
            lmp.setIdReserva(1L);
            lmp.setIdPropiedad(1L);
            lmp.setEstadoLimpieza(EstadoLimpieza.PENDIENTE);
            lmp.setFechaProgramada(LocalDateTime.now().plusDays(3));
            lmp.setFechaRealizada(null);
            lmp.setMotivo("Inicialización de datos del sistema");
            limpiezaRepository.save(lmp);
        }
    }
}
