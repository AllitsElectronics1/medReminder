package com.medReminder.controller.web;

import com.medReminder.entity.Device;
import com.medReminder.entity.Patient;
import com.medReminder.service.DeviceService;
import com.medReminder.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/devices")
@RequiredArgsConstructor
@Slf4j
public class DeviceWebController {

    private final DeviceService deviceService;
    private final PatientService patientService;

    @GetMapping
    public String showDevicesList(Model model) {
        List<Patient> patients = patientService.getPatientsForCurrentUser();
        model.addAttribute("patients", patients);
        return "device/list";
    }

    @GetMapping("/add")
    public String showAddDeviceForm(Model model) {
        List<Patient> patients = patientService.getPatientsForCurrentUser();
        model.addAttribute("device", new Device());
        model.addAttribute("patients", patients);
        return "device/add";
    }

    @GetMapping("/{id}")
    public String showDeviceDetails(@PathVariable Long id, Model model) {
        Device device = deviceService.getDeviceById(id);
        model.addAttribute("device", device);
        return "device/details";
    }

    @GetMapping("/{id}/edit")
    public String showEditDeviceForm(@PathVariable Long id, Model model) {
        Device device = deviceService.getDeviceById(id);
        List<Patient> patients = patientService.getPatientsForCurrentUser();
        
        model.addAttribute("device", device);
        model.addAttribute("patients", patients);
        return "device/edit";
    }

    @GetMapping("/patient/{patientId}")
    public String showDevicesForPatient(@PathVariable Long patientId, Model model) {
        List<Device> devices = deviceService.getDevicesByPatientId(patientId);
        Patient patient = patientService.getPatientById(patientId);
        
        model.addAttribute("devices", devices);
        model.addAttribute("patient", patient);
        return "device/list";
    }
} 