
# MIU Academic Record Management System

## Overview
This document serves as the presentation script for the **MIU Academic Record Management** application, a full-stack academic record tracking and management application. The presentation is delivered by three developers: Abdullah al Zayed (Cloud Architect), Olamide Boluwatife Akinoso (Solutions Architect and Backend Engineer),and Hnin Nandar Zaw (Frontend Engineer). It covers the SDLC phases, solution architecture, design models, and deployment strategies.

---

## 1. Introduction and SDLC Overview (Presented by Olamide - Solutions Architect)

###  1: Title
**MIU Academic Record Management System**  
A Full-Stack Solution for Seamless Academic tracking system  
Presenters: Olamide (Solutions Architect and Backend Engineer), Nandar (Frontend Engineer), Abdullah (DevOps Engineer)  
Date: August 13, 2025


---

###  2: Introduction to MIU Academic Record Management System
**MIU Academic Record Management System (ARMS)**
- ARMS is a web-based platform designed to streamline the management of student academic records, course registrations, and grading workflows, while offering powerful administrative tools for faculty and staff.
- Built as a full-stack application with a React frontend and Spring Boot backend, ensuring a responsive user experience and scalable server-side operations.
- Developed following the Software Development Life Cycle (SDLC) to maintain a structured, efficient, and quality-driven development process.


---

###  3: SDLC Overview
**Software Development Life Cycle (SDLC)**
- **Planning**: Define project goals, scope, and feasibility.
- **Requirements Analysis**: Gather and analyze user requirements and use cases.
- **Design**: Create system architecture, domain models, and database design.
- **Implementation**: Develop the frontend and backend components.
- **Testing**: Validate functionality through unit and integration tests.
- **Deployment**: Deploy the application to production environments.
- **Maintenance**: Monitor, scale, and plan future enhancements.

---

## 2 Planning and Requirements Analysis (Presented by Olamide - Solutions Architect)

###  4: Planning Phase
**Planning Phase**
- **Objective**: Build a scalable academic record management system to support student data tracking, course registration, and grading workflows.
- **Scope**:
    - Core features: Core features: Student profile management, course enrollment, grade submission, and administrative reporting.
    - Target users: University students, faculty members, and academic administrators.
- **Feasibility**:
    - Technical: Feasible with React for the frontend, Spring Boot for the backend, and AWS for cloud deployment.
    - Economic: Cost-effective by leveraging open-source technologies and scalable cloud infrastructure.
- **Team Roles**:
    - Olamide: Solutions Architect and Backend Engineer (architecture, API, and database).
    - Nandar: Frontend Engineer (UI/UX).
    - Abdullah: DevOps Engineer (deployment and CI/CD).

---

###  5: Problem Scenario (Requirements Analysis)
**Problem Scenario**
- **Student Pain Points**:
    - Difficulty accessing academic records, course schedules, and grade reports in one place.
    - Limited transparency in course registration status and academic progress tracking.
- **Faculty/Admin Pain Points**:
    - Inefficient tools for managing student records, submitting grades, and generating academic reports.
    - Lack of role-based access control, making it hard to enforce data security and operational boundaries.
- **Institutional Need**:
    - A secure, user-friendly, and scalable system to manage academic records, streamline registration, and support grading workflows.
    - Support for multiple user roles including students, faculty, and academic administrators.

<img width="2416" height="1228" alt="image" src="https://github.com/user-attachments/assets/c54921c0-b013-4da3-adde-1f78d7eb998c" />

---

###  6: Use Cases and User Stories (Requirements Analysis)
**Use Cases and User Stories**
- Identified use cases for three primary user roles: **Students**, **Faculty**, and **Admins**.

**Student Use Cases**:
- View Academic Records: As a student, I want to view my academic records including grades and course history, so I can track my progress.
- Register for Courses: As a student, I want to register for available courses each term, so I can plan my academic schedule.
- View Course Schedule: As a student, I want to see my course schedule, so I can manage my time effectively.

**Faculty Use Cases**:
- Submit Grades: As a faculty member, I want to submit grades for students in my courses, so I can complete my teaching responsibilities.
- View Enrolled Students: As a faculty member, I want to view the list of students enrolled in my courses, so I can manage attendance and grading.

**Registrar Use Cases**:
- Manage Student Records: As a registrar, I want to create, update, and archive student records, so I can maintain accurate academic data.
- Manage Course Catalog: As a registrar, I want to add, update, and remove courses from the catalog, so I can ensure offerings are current.
- Assign Student to Courses: As a registrar, I want to assign students to courses, so I can coordinate course assignments.
- Assign Student to Classroom: As a registrar, I want to assign students to classroom, so I can coordinate classroom assignments.

**Ad,min Use Cases**:
- Manage User profile: As an admin, I want to create, update, and manage user profile, so they can access the system.
- Manage Role: As an admin, I want to add, update, and assign roles to users, so users have access to the only features recommended by the institution.
- 
**Diagram Placeholder**:  
<img width="470" height="844" alt="image" src="https://github.com/user-attachments/assets/10b3df6b-75c1-48cd-89af-1e3f16f06784" />

*Note*: Include a use case diagram showing actors (Student, Faculty, and Admin) and use cases (View Academic Records, Register for Courses, and Submit Grades etc.) with relationships.

*User Story Map*
<img width="1500" height="598" alt="image" src="https://github.com/user-attachments/assets/433108ca-b6d2-49cc-bae9-0fc730afb2ec" />

---

## 3. Design Phase (Presented by Olamide - Backend Engineer)

###  7: Solution Architecture
**Solution Architecture**
- **Frontend**: React with TypeScript, Material-UI
- **Backend**: Spring Boot with JPA, MySQL database, deployed on AWS EC2.
- **Communication**: RESTful API with JSON payloads, secured with JWT authentication.
- **Infrastructure**: AWS services (S3, EC2, CloudWatch for monitoring, Auto Scaling for scalability).

**Diagram Placeholder**:  
<img width="930" height="974" alt="architecture" src="https://github.com/user-attachments/assets/b2e70b77-dbe3-464d-83a6-e0c11301f2ef" />

*Note*: Include a diagram showing:
- Frontend (React) on S3.
- Backend (Spring Boot) on EC2 with MySQL.
- API calls between frontend and backend.
- AWS services (CloudWatch, Auto Scaling) for monitoring and scaling.

---

###  8: Domain Model (Class Diagram)
**Domain Model: Class Diagram**
- **Entities**:
    - Student: id, firstName, lastName, dob, contactInfo etc.
    - Course: courseId, courseCode, title, creditHours
    - Enrollment: enrollmentId, semester, grade, completionStatus
    - Classroom: classroomId, name, capacity
    - StudentClassroom: id, semester, startDate, endDate
    - Transcript: transcriptId, gpa, issueDate
    - Role: id, name(ADMIN, REGISTRAR, FACULTY, STUDENT)
- **Relationships**:
    - User ↔ Role: Many-to-Many
    - Student ↔ Enrollment ↔ Course: Many-to-Many (via Enrollment)
    - Student ↔ StudentClassroom ↔ Classroom: Many-to-Many (via StudentClassroom)
    - Course ↔ Course (prerequisites): Self-referencing Many-to-Many
    - Transcript ↔ Student: One-to-One (or One-to-Many if multiple transcripts over time)
    <img width="2188" height="1284" alt="image" src="https://github.com/user-attachments/assets/62299168-ca57-4e08-884c-3258ab31c738" />

---

###  9: Sequence Diagram ()
**Sequence Diagram: Student Enrollment Process**
- **Actors**: Registrar Staff, Faculty, Student, Frontend (React), Backend (Spring Boot), Database
- **Steps**:
    1. Registrar Staff logs into ARMS via the Web Interface.
    2. Web Interface sends credentials to AuthController for authentication.
    3. AuthController queries the Database to validate credentials.
    4. Database returns user role (Registrar) to AuthController.
    5. AuthController confirms successful authentication to the Web Interface.
    6. Registrar selects “Enroll Student” and searches by Student ID.
    7. Web Interface requests student details from EnrollmentController.
    8. EnrollmentController queries the Database for student data and returns it.
    9. Registrar selects courses for the student’s enrollment.
  10. Web Interface requests available courses from EnrollmentController.
  11. EnrollmentController queries the Database for course offerings and returns the list.
  12. Registrar confirms enrollment in multiple courses.
  13. Web Interface sends the enrollment request to EnrollmentController.
  14. EnrollmentController creates Enrollment records in the Database.
  15. Database confirms enrollment creation.
  16. EnrollmentController sends confirmation to the Web Interface for display.
  <img width="3118" height="3840" alt="ARMS-student-sequence-diagram" src="https://github.com/user-attachments/assets/d6fe0cd2-f615-426a-9535-8b2ae9b20542" />

---

###  10: Software Design Patterns and Principles
**Software Design Patterns and Principles**
- **Design Patterns**:
    - Repository Pattern: Implemented in StudentRepository, CourseRepository, ClassroomRepository, and linking-entity repositories (EnrollmentRepository, StudentClassroomRepository) to encapsulate database operations.
    - Dependency Injection: Used in Spring Boot via @Autowired for injecting services and repositories into controllers.
    - Model-View-Controller (MVC): The system separates concerns between Controllers (handling requests), Services (business logic), and Repositories (data persistence).
- **SOLID Principles**:
    - Single Responsibility Principle (SRP): Each class has one responsibility.
    - Open/Closed Principle (OCP): System is extensible.
    - Dependency Inversion Principle (DIP): High-level modules depend on abstractions.
- **Other Principles**:
    - DRY: Reused components (e.g., NavButton in frontend).
    - KISS: Kept API design simple (RESTful endpoints).

---

## 4. Implementation Phase (Presented by Olamide - Backend Engineer and Nandar - Frontend Engineer)

###  11: Backend Implementation (Olamide)
**Backend Implementation**
- Built with Spring Boot, JPA, and MySQL.
- **Key Components**:
    - StudentController: Handles CRUD operations for students (POST /api/students, GET /api/students/{id}, etc.).
    - CourseController: Manages courses and enrollments (POST /api/courses, GET /api/courses/{id}, POST /api/courses/{courseId}/enroll).
    - ClassroomController: Manages classrooms and student assignments (POST /api/classrooms, GET /api/classrooms/{id}, POST /api/classrooms/{classroomId}/assign-student).
    - Repositories: Implemented via JPA for Student, Course, Enrollment, Classroom, and StudentClassroom.
- **Challenges and Solutions**:
    - Challenge: Managing many-to-many relationships for Student–Course and Student–Classroom.
    - Solution: Created explicit linking entities (Enrollment and StudentClassroom) to store association data and handle additional fields (e.g., enrollment date).

*Note*: Show Swagger UI with endpoints
https://arms-webapp.azurewebsites.net/swagger-ui/index.html

---

###  12: Frontend Implementation (Nandar)
**Frontend Implementation**
- Built with React, TypeScript, and Material-UI.
- **Key Components**:
    - Header: Dynamic navbar with role-based links.
    - Management Dashboard: Tabbed interface for managing students, courses, and enrollments.
    - Login/Signup: Modal for user authentication.
- **Challenges and Solutions**:
    - Challenge: Handling nested data.
    - Solution: Used optional chaining (?.) and fallback values (|| 'N/A').
    - Challenge: Form validation for enrollment.
    - Solution: Added client-side validation.


---

###  13: UI/UX Design (Nandar)
**UI/UX Design**
- Consistent theme .
- Responsive design with Material-UI components (e.g., Table, Dialog, Tabs).
- User feedback: error alerts, confirmation dialogs.


---

## 5. Testing Phase (Presented by Abdullah)

###  14: Testing Strategy
**Testing Strategy**
- **Backend Testing (Olamide)**:
    - Unit Tests: Used JUnit to test controllers, services, and repositories.
    - Integration Tests: Tested API endpoints with MockMvc.
    - Example: Tested POST /api/student to ensure correct enrollment
- **Frontend Testing (Nandar)**:
    - Unit Tests: Used Jest and React Testing Library to test components.
- **Test Coverage**:
    - Backend: 85% coverage.
    - Frontend: 80% coverage.

---

## 15. Deployment

**Deployment summary (Azure)**
- **Containerize the app (Spring Boot):** build a Docker image that listens on 0.0.0.0:8080, push it to Azure Container Registry (ACR).
    <img width="940" height="454" alt="image" src="https://github.com/user-attachments/assets/3311e4ac-6ed1-4467-b37c-9010d6af039c" />
- **Create Web App for Containers and point it to the ACR image** (use Managed Identity + AcrPull).
  <img width="1512" height="849" alt="image" src="https://github.com/user-attachments/assets/9ba9480e-719d-4baa-8bf1-29a87998a7ae" />

- **Provision Azure Database for MySQL – Flexible Server** (dev: public access enabled), create the DB and an app user, and grant privileges.
  <img width="1608" height="846" alt="image" src="https://github.com/user-attachments/assets/eaf73ce2-84c2-4332-ab83-471785e882e2" />

- **Configure App Settings on the Web App** (no secrets in the image):
    WEBSITES_PORT=8080, SERVER_ADDRESS=0.0.0.0, SERVER_PORT=8080
    SPRING_DATASOURCE_URL=jdbc:mysql://<server>.mysql.database.azure.com:3306/<db>?sslMode=REQUIRED
    SPRING_DATASOURCE_USERNAME=<user>, SPRING_DATASOURCE_PASSWORD=<password>
- Enable container logs, restart the app, and verify health (optionally set /actuator/health as Health check).


---

## 6. Conclusion and Future Work 

###  15: Summary
**Conclusion**
- The Academic Records Management System (ARMS) successfully followed the SDLC to deliver a secure, reliable, and maintainable system for managing student academic records.
- Key features: Student registration, course management, enrollment, grade recording, classroom assignments, and transcript generation.
- Applied modern software engineering practices: Design patterns, SOLID principles, layered architecture, Spring Boot with profiles, Maven build automation

---

###  16: Future Work (Maintenance Phase)
**Future Work**
- Implement advanced reporting features (e.g., GPA trends, semester performance summaries).
- Add role-specific dashboards for faculty, registrar staff, and students.
- Integrate notification features for enrollment confirmations and grade updates.

---

###  17: Closing
**Closing**
- Thank you for your attention!
- We welcome your questions.

---

## 7. Q&A Session (All Presenters)
**Q&A Session**
- Open the floor for questions from the audience.
- Each team member will answer questions related to their respective sections.

---
