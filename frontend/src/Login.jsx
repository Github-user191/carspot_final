import React, { useEffect, useState } from 'react'
import { LandingPageNavigationBar } from './common/NavigationBar';
import { Link } from 'react-router-dom';
import { IoMdEye } from 'react-icons/io'
import { IoMdEyeOff } from 'react-icons/io'
import { FcGoogle } from 'react-icons/fc'
import { FaFacebookF } from 'react-icons/fa'
import { ImGithub } from 'react-icons/im'
import { GoCheck } from 'react-icons/go'
import AuthenticationService from './services/AuthenticationService';
import { Bounce, Slide, toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router';
import { FACEBOOK_AUTH_URL, GOOGLE_AUTH_URL } from './utils';
import ForgotPasswordFormModal from './ForgotPasswordFormModal';
import LoadingComponent from './common/LoadingComponent';
import classnames from "classnames";
import carFormBackground from "./assets/car-form-background.jpg";
import FormComponent from './common/FormComponent';
import loginIlustration from './assets/login-illustration.svg'

const Login = () => {

    const [user, setUser] = useState({
        emailAddress: '',
        password: ''
    })

    const [errors, setErrors] = useState({
        emailAddressError: '',
        passwordError: '',
        forgotEmailAddressError: '',
        accountNotVerifiedError: '',
        authenticationError: ''
    })

    const handleChange = (event) => {
        const { name, value } = event.target;

        setUser(prev => ({
            ...prev, [name]: value
        }))

    }
    const navigate = useNavigate();


    const [loading, setLoading] = useState(false);


    const clearErrors = () => {
        setErrors({
          fullNameError: '',
          emailAddressError: '',
          mobileNumberError: '',
          passwordError: '',
          confirmPasswordError: '',
          emailAlreadyExistsError: ''
        })
      }
    
    
      const clearInput = () => {
        setUser({
          fullName: "",
          emailAddress: "",
          mobileNumber:  "+27",
          password: "",
          confirmPassword: "",
          emailAlreadyExists: ""
        });
      }

    const handleSubmit = (event) => {
        event.preventDefault();
        setLoading(true);


        AuthenticationService.authenticate(user)
            .then(res => {
                setLoading(false);
                navigate("/");
            }).catch(err => {
                const {accountNotVerified, emailAddress, password, invalidToken, authenticationError} = err.response.data;
                setErrors({
                    emailAddressError: emailAddress,
                    passwordError: password,
                    accountNotVerifiedError: accountNotVerified,
                    forgotEmailAddressError: null,
                    authenticationError: authenticationError
                })
                setLoading(false)

                if (accountNotVerified) {
                    toast.error(accountNotVerified)
                }
            }).finally(() => setLoading(false))       
    }
    


    const [showPassword, setShowPassword] = useState(false);
    const togglePassword = () => {
        setShowPassword(!showPassword);
    };


    return (

        <div className='login-page-container'>
            <LandingPageNavigationBar />

            <div className="content">

                <div className="login-right-info">
                    <form className="login-form" onSubmit={handleSubmit}>

                        <div className="header">
                            <h3>Login</h3>

                            <Link to="/register" style={{ textDecoration: "none" }}>
                                <p>
                                    Don't have an account?{" "}<span style={{ color: "var(--primary-light-blue)", paddingLeft: ".05rem" }}>Create one</span>
                                </p>
                            </Link>

                        </div>

                        <div className="form-group">
                            <div className="mb-1">
                                <label> Email Address </label>
                                {/* {errors.accountNotVerifiedError ? <label id="form-error-status">{errors.accountNotVerifiedError}</label> : ''} */}
                                {errors.authenticationError ? <label id="form-error-status">{errors.authenticationError}</label> : ''}
                                {errors.emailAddressError ? <label id="form-error-status">{errors.emailAddressError}</label> : ''}
                                <input type="text" placeholder="Email address" onChange={handleChange} name="emailAddress" className="form-control shadow-none" value={user.emailAddress} />
                            </div>

                            <div className="mb-1">
                                <label> Password </label>
                                <Link to='' onClick={togglePassword}>
                                    {showPassword ? <IoMdEyeOff className="password-icon" /> : <IoMdEye className="password-icon" />}
                                </Link>

                    

                                {errors.passwordError ? <label id="form-error-status">{errors.passwordError}</label> : ''}
                                <input type={showPassword ? "text" : "password"} onChange={handleChange} placeholder="Password" name="password" className="form-control shadow-none" value={user.password} />
                            </div>
                            <div className='btn-container'>

                                <ForgotPasswordFormModal />
                                <button className='login-btn '>{loading ? <LoadingComponent color={"#fff"} /> : 'Login'}</button>
                            </div>
                        </div>




                    </form>

                    <ToastContainer />
                </div>




            </div>



        </div>

    )
}

export default Login
