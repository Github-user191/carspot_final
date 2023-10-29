import axios from "axios";
import React from "react";
import {Link} from "react-router-dom";
import {useEffect} from "react";
import {useState} from "react";
import {useParams, useNavigate} from "react-router";
import AuthenticationService from "../services/AuthenticationService";
import {Bounce, Slide, toast, ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import {IoMdEye} from 'react-icons/io'
import {IoMdEyeOff} from 'react-icons/io'
import {GoCheck} from 'react-icons/go'
import LoadingComponent from './LoadingComponent';

const ResetPasswordForm = (props) => {

    const [user, setUser] = useState({
        newPassword: "",
        confirmPassword: ""
    })

    const [loading, setLoading] = useState(false);
    const [errors, setErrors] = useState({
        newPasswordError: "",
        confirmPasswordError: "",
        tokenError: ""
    })

    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    const togglePassword = () => {
        setShowPassword(!showPassword);
    };

    const toggleConfirmPassword = () => {
        setShowConfirmPassword(!showConfirmPassword);
    };

    // const [validToken, setValidToken] = useState();

    const {token} = useParams();

    const navigate = useNavigate();


    const handleChange = (event) => {
        const {name, value} = event.target;

        setUser(prev => ({
            ...prev, [name]: value
        }))

    }

    const notifySuccess = (message) => {
        toast.success(message);
    }

    const notifyError = (message) => {
        toast.error(message);
    }


    useEffect(() => {

        AuthenticationService.confirmPasswordReset(token)
            .then(res => {
            }).catch(err => {
            setErrors({
                passwordError: '',
                tokenError: err.response.data.invalidToken
            })
        })

    }, []);

    const handleSubmit = (event) => {
        event.preventDefault();

        setErrors({
            newPasswordError: '',
            confirmPasswordError: ''
        })

        if (user.newPassword !== user.confirmPassword) {
            setErrors({
                confirmPasswordError: "Passwords do not match"
            })

        } else {
            AuthenticationService.resetPassword(user, token)
                .then((res) => {
                    notifySuccess(res.data.message)

                    setUser({
                        newPassword: '',
                        confirmPassword: ''
                    })

                    setTimeout(() => {
                        navigate("/login");
                    }, [1000])
                })
                .catch((err) => {
                    setErrors({
                        newPasswordError: err.response.data.badRequestException,
                    })
                    if (err.response.data.invalidToken) {
                        notifyError(err.response.data.invalidToken)

                        setTimeout(() => {
                            navigate("/login");
                        }, [1000])
                    }

                });
        }

    }


    return (
        <div className="reset-password-page">

            <form className="reset-password-form" onSubmit={handleSubmit}>
                <div className="header mb-3">
                    <h3>
                        Reset password
                    </h3>
                    <p>
                        Forgot your password? Reset it
                    </p>
                </div>

                <div className="form-group">
                    <div className="mt-2">

                        <Link to='' onClick={togglePassword}>
                            {showPassword ? <IoMdEyeOff className="password-icon"/> :
                                <IoMdEye className="password-icon"/>}
                        </Link>

                        <label> New Password </label>
                        {errors.newPasswordError ? <label id="form-error-status">{errors.newPasswordError}</label> : ''}

                        <input type={showPassword ? "text" : "password"} placeholder="Password"
                               className="form-control shadow-none" name="newPassword" value={user.newPassword}
                               onChange={handleChange}/>


                    </div>

                    <div className="mt-2">

                        <Link to='' onClick={toggleConfirmPassword}>
                            {showPassword ? <IoMdEyeOff className="password-icon"/> :
                                <IoMdEye className="password-icon"/>}
                        </Link>

                        <label> Confirm Password </label>
                        {errors.confirmPasswordError ?
                            <label id="form-error-status">{errors.confirmPasswordError}</label> : ''}

                        <input type={showConfirmPassword ? "text" : "password"} placeholder="Confirm password"
                               className="form-control shadow-none" name="confirmPassword" value={user.confirmPassword}
                               onChange={handleChange}/>


                    </div>

                    <div className="btn-container mt-3">

                        <button className="reset-btn">
                            {loading ? <LoadingComponent color={"#fff"}/> : 'Change'}
                        </button>


                    </div>


                </div>


            </form>
            <ToastContainer/>
        </div>
    );
};

export default ResetPasswordForm;
