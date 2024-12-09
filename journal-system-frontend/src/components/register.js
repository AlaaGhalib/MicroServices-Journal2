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

  const onSubmit = async (data) => {
    const { username, password, confirmpwd, mobile, role, name, address, date_of_birth } = data;

    // Password confirmation check
    if (password !== confirmpwd) {
      alert("Passwords do not match.");
      return;
    }

    // Build the user data object
    const userData = {
      username,
      password,
      phoneNumber: mobile,
      role,
      name: name || null,
      address: address || null,
      dateOfBirth: date_of_birth || null,
    };

    try {
      const response = await axios.post('http://localhost:8080/api/user/register', userData);
      if (response.status === 201) {
        alert("User registered successfully");
        navigate('/'); // Redirect to login after successful registration
      }
    } catch (error) {
      if (error.response) {
        // Check for conflict errors (HTTP status 409)
        if (error.response.status === 409) {
          alert(error.response.data); // Display conflict error message (e.g., username or phone already exists)
        }
        // Handle other specific error statuses (e.g., 500 Internal Server Error)
        else if (error.response.status === 500) {
          alert('Internal server error occurred. Please try again later.');
        }
        // Handle other possible error statuses (e.g., 400 Bad Request)
        else if (error.response.status === 400) {
          alert('Bad Request. Please check the input fields and try again.');
        }
        // Handle unexpected errors
        else {
          console.error("Unexpected error:", error);
          alert('An unexpected error occurred. Please try again.');
        }
      }
      // Handle errors where no response is received
      else if (error.request) {
        console.error("No response received:", error.request);
        alert('Network error. Please check your internet connection and try again.');
      }
      // Handle general errors (e.g., unexpected issues in Axios itself)
      else {
        console.error('Error in setting up request:', error.message);
        alert('An error occurred while setting up the request.');
      }
    }
  };

  return (
    <section>
      <div className="register">
        <div className="col-1">
          <h2 className="signup-heading">Sign Up</h2>
          <span>Register and enjoy the service</span>

          <form id='form' className='flex flex-col' onSubmit={handleSubmit(onSubmit)}>
            {/* Username */}
            <input type="text" {...register("username", { required: true })} placeholder='Username' />
            {errors.username && <p className="error">Username is required</p>}

            {/* Password */}
            <input type="password" {...register("password", { required: true })} placeholder='Password' />
            {errors.password && <p className="error">Password is required</p>}

            {/* Confirm Password */}
            <input type="password" {...register("confirmpwd", { required: true })} placeholder='Confirm Password' />
            {errors.confirmpwd && <p className="error">Password confirmation is required</p>}

            {/* Mobile */}
            <input type="text" {...register("mobile", { required: true, maxLength: 10 })} placeholder='Mobile Number' />
            {errors.mobile?.type === "required" && <p className="error">Mobile Number is required</p>}
            {errors.mobile?.type === "maxLength" && <p className="error">Max Length Exceeded</p>}

            {/* Role */}
            <select {...register("role", { required: true })} onChange={(e) => setSelectedRole(e.target.value)}>
              <option value="">Select Role</option>
              <option value="PATIENT">Patient</option>
              <option value="DOCTOR">Doctor</option>
              <option value="STAFF">Staff</option>
            </select>
            {errors.role && <p className="error">Role is required</p>}

            {/* Additional Fields for Patients */}
            {(selectedRole === 'PATIENT' || selectedRole === 'DOCTOR' || selectedRole === 'STAFF') && (
              <>
                <input type="text" {...register("name", { required: true })} placeholder='Name' />
                {errors.name && <p className="error">Name is required</p>}

                <input type="text" {...register("address", { required: true })} placeholder='Address' />
                {errors.address && <p className="error">Address is required</p>}

                <input type="text" {...register("date_of_birth", { required: true })} placeholder='Date of Birth (YYYY-MM-DD)' />
                {errors.date_of_birth && <p className="error">Date of Birth is required</p>}
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
