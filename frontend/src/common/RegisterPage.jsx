import React, {useState} from 'react'
import {IoMdEye} from 'react-icons/io'
import {IoMdEyeOff} from 'react-icons/io'
import {LandingPageNavigationBar} from '../common/NavigationBar';
import {Link} from 'react-router-dom';
import AuthenticationService from '../services/AuthenticationService';
import {GoCheck} from 'react-icons/go'
import PhoneInput from 'react-phone-input-2'
import 'react-phone-input-2/lib/style.css'
import {toast, ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import LoadingComponent from '../common/LoadingComponent';
import loginIlustration from '../assets/login-illustration.svg'

const RegisterPage = () => {

    const [user, setUser] = useState({
        fullName: "",
        emailAddress: "",
        mobileNumber: "",
        password: "",
        confirmPassword: "",
        emailAlreadyExists: ""
    });

    let passwordLength;
    let confirmPasswordLength;
    const [loading, setLoading] = useState(false);


    const [errors, setErrors] = useState({
        fullNameError: '',
        emailAddressError: '',
        mobileNumberError: '',
        passwordError: '',
        confirmPasswordError: '',
        emailAlreadyExistsError: ''
    })
    // mobileNumber => setUser({ mobileNumber:mobileNumber })
    const handlePhoneInputChange = (event) => {
        setUser(prev => ({
            ...prev, mobileNumber: event
        }))
    }
    const handleChange = (event) => {
        const {name, value} = event.target;
        setUser((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

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
            mobileNumber: "+27",
            password: "",
            confirmPassword: "",
            emailAlreadyExists: ""
        });
    }

    const handleSubmit = (event) => {
        event.preventDefault();

        setLoading(true);
        AuthenticationService.signUp(user)
            .then(res => {


                toast.success(res.data.message);

                clearInput();
                clearErrors();
                setLoading(false)
            }).catch(err => {
            setLoading(false);
            setErrors({
                fullNameError: err.response.data.fullName,
                emailAddressError: err.response.data.emailAddress,
                mobileNumberError: err.response.data.mobileNumber,
                passwordError: err.response.data.password,
                confirmPasswordError: err.response.data.confirmPassword,
                emailAlreadyExistsError: err.response.data.emailAlreadyExists
            })
        })


    }

    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    const togglePassword = () => {
        setShowPassword(!showPassword);
    };

    const toggleConfirmPassword = () => {
        setShowConfirmPassword(!showConfirmPassword);
    };

    return (


        <div className="register-page-container">
            <LandingPageNavigationBar/>

            <div className="content">

                <div className="register-right-info">
                    <form className="register-form" onSubmit={handleSubmit}>
                        <div className="header">
                            <h3>
                                Register with CarSpot
                            </h3>

                            <Link to="/login" style={{textDecoration: "none"}}>
                                <p>
                                    Already have an account?{" "}<span
                                    style={{color: "var(--primary-light-blue)", paddingLeft: ".05rem"}}>Sign in</span>
                                </p>
                            </Link>
                            {/* <p className="info">It only takes a few minutes </p> */}
                        </div>

                        <div className="form-group">

                            <div className="mb-2">


                                <label>Full name</label>
                                {user.fullName && !errors.emailAlreadyExistsError ?
                                    <GoCheck size={15} id="checkmark-success-status"/> :
                                    <label id="form-error-status">{errors.fullNameError}</label>}

                                {errors.emailAlreadyExistsError ?
                                    <label id="form-error-status">{errors.emailAlreadyExistsError}</label> : ''}
                                <input
                                    type="text"
                                    placeholder="Full name"
                                    className="form-control shadow-none full-name"
                                    onChange={handleChange}
                                    name="fullName"
                                    value={user.fullName}
                                />


                            </div>

                            <div className="mb-2">


                                <label> Email address </label>
                                {user.emailAddress.length > 8 && user.emailAddress.includes("@", -1) ?
                                    <GoCheck size={15} id="checkmark-success-status"/> :
                                    <label id="form-error-status">{errors.emailAddressError}</label>}

                                <input
                                    type="email"
                                    placeholder="Email address"
                                    className="form-control shadow-none email-address"
                                    onChange={handleChange}
                                    name="emailAddress"
                                    value={user.emailAddress}
                                />

                            </div>

                            <div className="mb-2">
                                {/* <input
                type="tel"
                placeholder="Mobile number"
                className="form-control shadow-none mobile-number"
                onChange={handleChange}
                name="mobileNumber"
                value={user.mobileNumber}
              /> */}


                                <label> Mobile Number</label>
                                {user.mobileNumber.length > 10 ? <GoCheck size={15} id="checkmark-success-status"/> :
                                    <label id="form-error-status">{errors.mobileNumberError}</label>}

                                <PhoneInput
                                    buttonStyle={{backgroundColor: "transparent", border: "none"}}
                                    buttonClass="mobile-number-btn"
                                    country={'za'}
                                    disableDropdown
                                    inputClass="form-control mobile-number-btn shadow-none"
                                    inputStyle={{width: "100%"}}
                                    masks={{za: '.. ... ....'}}
                                    value={user.mobileNumber}
                                    name="mobileNumber"
                                    onChange={handlePhoneInputChange}
                                    countryCodeEditable={false}
                                    placeholder='00 000 0000'
                                />

                            </div>

                            <div className="mb-2">

                                <Link to='' onClick={togglePassword}>
                                    {showPassword ? <IoMdEyeOff className="password-icon"/> :
                                        <IoMdEye className="password-icon"/>}
                                </Link>


                                <label>Password</label>
                                {user.password.length > 5 ? <GoCheck size={15} id="checkmark-success-status"/> :
                                    <label id="form-error-status">{errors.passwordError}</label>}

                                <input
                                    type={showPassword ? "text" : "password"}
                                    placeholder="Password"
                                    className="form-control shadow-none password"
                                    name="password"
                                    value={user.password}
                                    onChange={handleChange}

                                />

                            </div>

                            <div className="mb-4">


                                {/* {errors.confirmPasswordError ? <p id="text-status">Passwords do not match</p> : ''} */}
                                <Link to='' onClick={toggleConfirmPassword}>
                                    {showConfirmPassword ? <IoMdEyeOff className="password-icon"/> :
                                        <IoMdEye className="password-icon"/>}
                                </Link>


                                <label> Confirm password</label>
                                {user.confirmPassword === user.password && user.confirmPassword.length > 5 ?
                                    <GoCheck size={15} id="checkmark-success-status"/> :
                                    <label id="form-error-status">{errors.confirmPasswordError}</label>}

                                <input
                                    type={showConfirmPassword ? "text" : "password"}
                                    placeholder="Confirm password"
                                    className="form-control shadow-none confirm-password"
                                    onChange={handleChange}
                                    name="confirmPassword"
                                    value={user.confirmPassword}
                                />

                            </div>

                            <div className="btn-container mt-2">
                                <button className="register-btn">
                                    {loading ? <LoadingComponent color={"#fff"}/> : 'Register'}
                                </button>
                            </div>

                        </div>
                    </form>
                    <ToastContainer style={{marginTop: "5rem"}}/>

                </div>
            </div>


        </div>


    );
};

export default RegisterPage;
