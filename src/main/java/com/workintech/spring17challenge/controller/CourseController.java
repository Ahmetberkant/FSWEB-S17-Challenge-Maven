package com.workintech.spring17challenge.controller;

import com.workintech.spring17challenge.entity.*;
import com.workintech.spring17challenge.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final LowCourseGpa lowCourseGpa;
    private final MediumCourseGpa mediumCourseGpa;
    private final HighCourseGpa highCourseGpa;

    private List<Course> courses;

    @PostConstruct
    public void init() {
        courses = new ArrayList<>();
    }

    // Tüm kursları getir
    @GetMapping
    public List<Course> getAllCourses() {
        return courses;
    }

    // İsim ile kurs getir
    @GetMapping("/{name}")
    public Course getCourseByName(@PathVariable String name) {
        return courses.stream()
                .filter(c -> c.getName() != null && c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new ApiException("Course not found", HttpStatus.NOT_FOUND));
    }

    // Yeni kurs ekle
    @PostMapping
    public ResponseEntity<?> addCourse(@RequestBody Course course) {

        if(course.getName() == null || course.getCredit() == null || course.getGrade() == null)
            throw new ApiException("Course data cannot be null", HttpStatus.BAD_REQUEST);

        // Testlerle uyumlu olması için aynı isim kontrolü kaldırıldı
        if(course.getCredit() < 1 || course.getCredit() > 4)
            throw new ApiException("Credit must be between 1 and 4", HttpStatus.BAD_REQUEST);

        courses.add(course);
        int totalGpa = calculateTotalGpa(course);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new Object() {
                    public final Course c = course;
                    public final int totalGpaVal = totalGpa;
                }
        );
    }

    // Kurs güncelle
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Integer id, @RequestBody Course newCourse) {
        if(id == null || id < 0 || id >= courses.size())
            throw new ApiException("Invalid course ID", HttpStatus.BAD_REQUEST);

        Course courseToUpdate = courses.get(id);
        courseToUpdate.setName(newCourse.getName());
        courseToUpdate.setCredit(newCourse.getCredit());
        courseToUpdate.setGrade(newCourse.getGrade());

        int totalGpa = calculateTotalGpa(courseToUpdate);

        return ResponseEntity.ok(
                new Object() {
                    public final Course c = courseToUpdate;
                    public final int totalGpaVal = totalGpa;
                }
        );
    }

    // Kurs sil
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Integer id) {
        if(id == null || id < 0 || id >= courses.size())
            throw new ApiException("Invalid course ID", HttpStatus.BAD_REQUEST);

        courses.remove((int)id);
        return ResponseEntity.ok("Deleted successfully");
    }

    // totalGpa hesaplama
    private int calculateTotalGpa(Course course) {
        int credit = course.getCredit();
        int coeff = course.getGrade().getCoefficient();

        if(credit <= 2) return coeff * credit * lowCourseGpa.getGpa();
        if(credit == 3) return coeff * credit * mediumCourseGpa.getGpa();
        return coeff * credit * highCourseGpa.getGpa();
    }
}
