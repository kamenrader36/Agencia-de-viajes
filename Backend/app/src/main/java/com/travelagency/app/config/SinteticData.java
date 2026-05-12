package com.travelagency.app.config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.travelagency.app.entities.TourPackage;
import com.travelagency.app.repositories.TourPackageRepository;

@Configuration
public class SinteticData {

    @Bean
    CommandLineRunner initDatabase(TourPackageRepository repository) {

        return args -> {
            if (repository.count() == 0) {

                List<TourPackage> packages = new ArrayList<>();

                TourPackage p1 = new TourPackage();
                p1.setName("Aventura en Torres del Paine");
                p1.setDestination("Patagonia, Chile");
                p1.setDescription("Trekking por los senderos más hermosos del mundo.");
                p1.setStartDate(LocalDate.now().plusMonths(1));
                p1.setEndDate(LocalDate.now().plusMonths(1).plusDays(7));
                p1.setPrice(1250000.0);
                p1.setCapacity(15);
                p1.setSeason("Verano");
                p1.setTripType("Aventura");
                packages.add(p1);

                TourPackage p2 = new TourPackage();
                p2.setName("Relajo en Punta Cana");
                p2.setDestination("República Dominicana");
                p2.setDescription("Todo incluido en las mejores playas del Caribe.");
                p2.setStartDate(LocalDate.now().plusMonths(2));
                p2.setEndDate(LocalDate.now().plusMonths(2).plusDays(10));
                p2.setPrice(980000.0);
                p2.setCapacity(20);
                p2.setSeason("Verano");
                p2.setTripType("Aventura");
                packages.add(p2);

                TourPackage p3 = new TourPackage();
                p3.setName("San Pedro de Atacama");
                p3.setDestination("Antofagasta, Chile");
                p3.setDescription("Tour astronómico y visita a los géiseres del Tatio.");
                p3.setStartDate(LocalDate.now().plusWeeks(3));
                p3.setEndDate(LocalDate.now().plusWeeks(4));
                p3.setPrice(450000.0);
                p3.setCapacity(10);
                p3.setSeason("Verano");
                p3.setTripType("Aventura");
                packages.add(p3);

                TourPackage p4 = new TourPackage();
                p4.setName("Cultura en Kioto y Tokio");
                p4.setDestination("Japón");
                p4.setDescription("Explora templos antiguos y la modernidad tecnológica.");
                p4.setStartDate(LocalDate.now().plusMonths(6));
                p4.setEndDate(LocalDate.now().plusMonths(6).plusDays(14));
                p4.setPrice(3200000.0);
                p4.setCapacity(8);
                p4.setSeason("Verano");
                p4.setTripType("Aventura");
                packages.add(p4);

                TourPackage p5 = new TourPackage();
                p5.setName("Magia en Machu Picchu");
                p5.setDestination("Cusco, Perú");
                p5.setDescription("Descubre la ciudad perdida de los Incas.");
                p5.setStartDate(LocalDate.now().plusMonths(4));
                p5.setEndDate(LocalDate.now().plusMonths(4).plusDays(5));
                p5.setPrice(850000.0);
                p5.setCapacity(12);
                p5.setSeason("Verano");
                p5.setTripType("Aventura");
                packages.add(p5);

                TourPackage p6 = new TourPackage();
                p6.setName("Luces de París");
                p6.setDestination("Francia");
                p6.setDescription("Crucero por el Sena y visita guiada a la Torre Eiffel.");
                p6.setStartDate(LocalDate.now().plusMonths(5));
                p6.setEndDate(LocalDate.now().plusMonths(5).plusDays(8));
                p6.setPrice(2100000.0);
                p6.setCapacity(15);
                p6.setSeason("Verano");
                p6.setTripType("Aventura");
                packages.add(p6);

                TourPackage p7 = new TourPackage();
                p7.setName("Río de Janeiro Express");
                p7.setDestination("Brasil");
                p7.setDescription("Cristo Redentor, Pan de Azúcar y playas de Copacabana.");
                p7.setStartDate(LocalDate.now().plusMonths(1).plusWeeks(2));
                p7.setEndDate(LocalDate.now().plusMonths(1).plusWeeks(3));
                p7.setPrice(750000.0);
                p7.setCapacity(25);
                p7.setSeason("Verano");
                p7.setTripType("Aventura");
                packages.add(p7);

                TourPackage p8 = new TourPackage();
                p8.setName("Historia Viva en Roma");
                p8.setDestination("Italia");
                p8.setDescription("Coliseo, Vaticano y la mejor gastronomía italiana.");
                p8.setStartDate(LocalDate.now().plusMonths(7));
                p8.setEndDate(LocalDate.now().plusMonths(7).plusDays(9));
                p8.setPrice(1850000.0);
                p8.setCapacity(18);
                p8.setSeason("Verano");
                p8.setTripType("Aventura");
                packages.add(p8);

                TourPackage p9 = new TourPackage();
                p9.setName("Aventura en el Gran Cañón");
                p9.setDestination("Arizona, USA");
                p9.setDescription("Sobrevuelo en helicóptero y caminatas guiadas.");
                p9.setStartDate(LocalDate.now().plusMonths(8));
                p9.setEndDate(LocalDate.now().plusMonths(8).plusDays(6));
                p9.setPrice(1550000.0);
                p9.setCapacity(10);
                p9.setSeason("Verano");
                p9.setTripType("Aventura");
                packages.add(p9);

                TourPackage p10 = new TourPackage();
                p10.setName("Misterios de El Cairo");
                p10.setDestination("Egipto");
                p10.setDescription("Visita a las Pirámides de Giza y el Museo Egipcio.");
                p10.setStartDate(LocalDate.now().plusMonths(9));
                p10.setEndDate(LocalDate.now().plusMonths(9).plusDays(10));
                p10.setPrice(2400000.0);
                p10.setCapacity(14);
                p10.setSeason("Verano");
                p10.setTripType("Aventura");
                packages.add(p10);

                repository.saveAll(packages);
            }
        };
    }
}

