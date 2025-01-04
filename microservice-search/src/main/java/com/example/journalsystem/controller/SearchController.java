package com.example.journalsystem.controller;
import com.example.journalsystem.bo.Service.SearchService;
import com.example.journalsystem.bo.model.User;
import com.example.journalsystem.bo.model.Condition;
import com.example.journalsystem.bo.model.Encounter;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/search")
public class SearchController {

    @Inject
    SearchService searchService;
    @Data
    public class ConditionDTO {
        private Long id;
        private String diagnosis;
        private String status;
        private Long patientId;
    }

    @Data
    public static class EncounterDTO {
        private Long id;
        private LocalDateTime dateTime;
        private String reason;
        private Long patientId;
        private String notes;

        public static EncounterDTO fromEntity(Encounter encounter) {
            EncounterDTO dto = new EncounterDTO();
            dto.setId(encounter.getId());
            dto.setDateTime(encounter.getDateTime());
            dto.setReason(encounter.getReason());
            dto.setPatientId(encounter.getPatientId());
            dto.setNotes(encounter.getNotes());
            return dto;
        }
    }

    @Data
    public static class PatientDTO {
        private Long id;
        private String name;
        private String username;
        private String role;
        private String address;

        public static PatientDTO fromEntity(User user) {
            PatientDTO dto = new PatientDTO();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setUsername(user.getUsername());
            dto.setRole(user.getRole().toString());
            dto.setAddress(user.getAddress());
            return dto;
        }
    }

    @GET
    @Path("/patients/all")
    public Response getAllPatientsWithDetails() {
        List<PatientDTO> patientDTOs = searchService.getAllPatients()
                .stream()
                .map(PatientDTO::fromEntity)
                .collect(Collectors.toList());
        return Response.ok(patientDTOs).build();
    }

    @GET
    @Path("/patients")
    public Response searchPatientsByFirstName(@QueryParam("name") String firstName) {
        List<PatientDTO> patientDTOs = searchService.searchPatientsByFirstName(firstName)
                .stream()
                .map(PatientDTO::fromEntity)
                .collect(Collectors.toList());
        return Response.ok(patientDTOs).build();
    }

    @GET
    @Path("/patients/lastName")
    public Response searchPatientsByLastName(@QueryParam("lastName") String lastName) {
        List<PatientDTO> patientDTOs = searchService.searchPatientsByLastName(lastName)
                .stream()
                .map(PatientDTO::fromEntity)
                .collect(Collectors.toList());
        return Response.ok(patientDTOs).build();
    }

    @GET
    @Path("/patients/condition")
    public Response searchPatientsByCondition(@QueryParam("conditionName") String conditionName) {
        List<PatientDTO> patientDTOs = searchService.searchPatientsByCondition(conditionName)
                .stream()
                .map(PatientDTO::fromEntity)
                .collect(Collectors.toList());
        return Response.ok(patientDTOs).build();
    }

    @GET
    @Path("/patients/observations")
    public Response searchPatientsByObservation(@QueryParam("observationDetails") String observation) {
        List<PatientDTO> patientDTOs = searchService.searchPatientsByObservation(observation)
                .stream()
                .map(PatientDTO::fromEntity)
                .collect(Collectors.toList());
        return Response.ok(patientDTOs).build();
    }

    @GET
    @Path("/encounters")
    public Response searchEncounters(@QueryParam("practitionerId") Long practitionerId, @QueryParam("date") String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString);

            List<Encounter> encounters = searchService.searchEncounters(practitionerId, date);
            List<EncounterDTO> encounterDTOs = encounters.stream()
                    .map(EncounterDTO::fromEntity)
                    .collect(Collectors.toList());

            return Response.ok(encounterDTOs).build();
        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid date format. Use yyyy-MM-dd")
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
