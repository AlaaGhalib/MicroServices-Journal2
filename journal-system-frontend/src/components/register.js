import React, { useState } from 'react';
import bgImg from '../img1.jpg';
import { useForm } from 'react-hook-form';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../Register.css';

export default function RegisterForm() {
  const { register, handleSubmit, formState: { errors } } = useForm();
  const navigate = useNavigate();
  const [selectedRole, setSelectedRole] = useState('');

  // Hardcoded list of specialties (can be fetched from an API)
  const specialties = [
    "Cardiology",
    "Neurology",
    "Pediatrics",
    "Orthopedics",
    "Dermatology",
    "General Medicine",
    "Psychiatry",
    "Surgery",
    "Security",
    "Receptionist",
    "Janitorial & housekeeping",
    "Maintenance",
    "IT Support",
  ];

  const onSubmit = async (data) => {
    const { username, password, confirmpwd, mobile, role, specialty, name, address, date_of_birth } = data;

    // Password confirmation check
    if (password !== confirmpwd) {
      alert("Passwords do not match.");
      return;
    }

    let userData = {
      username,
      password,
      phoneNumber: mobile,
      role,
    };

    let url = '';
    if (role === 'PATIENT') {
      userData = { ...userData, name, address, dateOfBirth: date_of_birth };
      url = 'http://localhost:8080/api/register/patient';
    } else if (role === 'DOCTOR' || role === 'STAFF') {
      userData = { ...userData, name, specialty };
      url = 'http://localhost:8080/api/register/practitioner';
    } else {
      alert("Invalid role selected.");
      return;
    }

    try {
      console.log("User Data to Submit:", userData); // Log data being sent
      const response = await axios.post(url, userData);
      console.log("API Response:", response); // Log API response

      if (response.status === 201) {
        alert("User registered successfully");
        navigate('/'); // Redirect to login after successful registration
      }
    } catch (error) {
      console.error("Registration error:", error);
      alert("Registration failed. Please try again.");
    }
  };

  return (
    <section>
      <div className="register">
        <div className="col-1">
        <h2 className="signup-heading">Sign Up</h2>
        <span>Register and enjoy the service</span>

          <form id='form' className='flex flex-col' onSubmit={handleSubmit(onSubmit)}>
            {/* Common Fields */}
            <input type="text" {...register("username", { required: true })} placeholder='Username' />
            {errors.username && <p className="error">Username is required</p>}

            <input type="password" {...register("password", { required: true })} placeholder='Password' />
            {errors.password && <p className="error">Password is required</p>}

            <input type="password" {...register("confirmpwd", { required: true })} placeholder='Confirm Password' />
            {errors.confirmpwd && <p className="error">Password confirmation is required</p>}

            <input type="text" {...register("mobile", { required: true, maxLength: 10 })} placeholder='Mobile Number' />
            {errors.mobile?.type === "required" && <p className="error">Mobile Number is required</p>}
            {errors.mobile?.type === "maxLength" && <p className="error">Max Length Exceeded</p>}

            <select {...register("role", { required: true })} onChange={(e) => setSelectedRole(e.target.value)}>
              <option value="">Select Role</option>
              <option value="PATIENT">Patient</option>
              <option value="DOCTOR">Doctor</option>
              <option value="STAFF">Staff</option>
            </select>
            {errors.role && <p className="error">Role is required</p>}

            {/* Patient-Specific Fields */}
            {selectedRole === 'PATIENT' && (
              <>
                <input type="text" {...register("name", { required: true })} placeholder='Name' />
                {errors.name && <p className="error">Name is required</p>}

                <input type="text" {...register("address", { required: true })} placeholder='Address' />
                {errors.address && <p className="error">Address is required</p>}

                <input type="text" {...register("date_of_birth", { required: true })} placeholder='Date of Birth (YYYY-MM-DD)' />
                {errors.date_of_birth && <p className="error">Date of Birth is required</p>}
              </>
            )}

            {/* Doctor or Staff Specific Specialty Dropdown */}
            {(selectedRole === 'DOCTOR' || selectedRole === 'STAFF') && (
              <>
                <input type="text" {...register("name", { required: true })} placeholder='Name' />
                {errors.name && <p className="error">Name is required</p>}

                <select {...register("specialty", { required: true })}>
                  <option value="">Select Specialty</option>
                  {specialties.map((specialty, index) => (
                    <option key={index} value={specialty}>{specialty}</option>
                  ))}
                </select>
                {errors.specialty && <p className="error">Specialty is required</p>}
              </>
            )}

            <button className='btn' type="submit">Sign Up</button>
          </form>
        </div>

        <div className="col-2">
          <img src={bgImg} alt="Background" />
        </div>
      </div>
    </section>
  );
}
