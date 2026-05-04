package com.carservicetracker.repository;

import com.carservicetracker.model.Service;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ServiceRepository {

    private static final String FILE_PATH = "services.txt";
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public List<Service> findAll() {
        List<Service> services = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return services;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    Service s = new Service();
                    s.setId(Integer.parseInt(parts[0].trim()));
                    s.setName(parts[1].trim());
                    s.setDescription(parts[2].trim());
                    s.setDuration(parts[3].trim());
                    s.setPrice(Double.parseDouble(parts[4].trim()));
                    s.setCategory(parts[5].trim());
                    services.add(s);
                    idCounter.set(Math.max(idCounter.get(), s.getId() + 1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return services;
    }

    public void save(Service service) {
        if (service.getId() == 0) {
            service.setId(idCounter.getAndIncrement());
        }
        List<Service> services = findAll();
        services.removeIf(s -> s.getId() == service.getId());
        services.add(service);

        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Service s : services) {
                pw.println(s.getId() + "|" + s.getName() + "|" + s.getDescription() + "|" +
                        s.getDuration() + "|" + s.getPrice() + "|" + s.getCategory());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Service findById(int id) {
        return findAll().stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void delete(int id) {
        List<Service> services = findAll();
        services.removeIf(s -> s.getId() == id);
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Service s : services) {
                pw.println(s.getId() + "|" + s.getName() + "|" + s.getDescription() + "|" +
                        s.getDuration() + "|" + s.getPrice() + "|" + s.getCategory());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}