import React, {useState} from 'react'
import { Link, useNavigate } from 'react-router-dom';
import {IoMdEye} from 'react-icons/io'
import {IoMdEyeOff} from 'react-icons/io'
import MobileNavigationHeader from './MobileNavigationHeader'
import {GoCheck} from 'react-icons/go'
import LoadingComponent from './common/LoadingComponent';
import AuthenticationService from './services/AuthenticationService';
import { Bounce, Slide, toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

const ChangePasswordPage = () => {

    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const [user, setUser] = useState({
        oldPassword: "",
        newPassword: ""
    })

    const [errors, setErrors] = useState({
        oldPasswordError: "",
        newPasswordError: "",
        confirmNewPasswordError: ""
    })
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    const handleChange = (event) => {
        const {name, value} = event.target;

        setUser(prev => ({
            ...prev, [name] : value
        }))
    }

    const handleSubmit = (event) => {
        event.preventDefault();

        setErrors({
            newPasswordError: "",
            oldPasswordError: ""
        })

        setLoading(true)
        AuthenticationService.changePassword(user)  
            .then(res => {
                toast.success("Password changed successfully! You will be prompted to login shortly")
                AuthenticationService.logout();
                setTimeout(() => {
                    navigate("/login")
                }, 1000)
            
                setLoading(false)
            }).catch(err => {
                if(err.response) {
                    setErrors({newPasswordError: err.response.data.passwordError})

                    if(err.response.data.passwordError === "Old password is invalid") {
                        setErrors({oldPasswordError: err.response.data.passwordError})
                    }
                    
                }
                setLoading(false)
            }).finally(() => setLoading(false))
    }

    const togglePassword = () => {
      setShowPassword(!showPassword);
    };

    const toggleConfirmPassword = () => {
        setShowConfirmPassword(!showConfirmPassword);
      };


    return (
        <div className="change-password-page">


            <MobileNavigationHeader heading={"Change your password"}/>
            <div className="content">
                <form className="change-password-form" onSubmit={handleSubmit}>
                    <div className="form-group mt-4">

                        <div className="mb-2">

                            <Link to='' onClick={togglePassword}>
                                {showPassword ? <IoMdEyeOff className="password-icon" /> : <IoMdEye className="password-icon" />} 
                            </Link>
                    
                            <label>Old password</label>
                            {user.oldPassword.length >= 6 ?  <GoCheck size={15} id="checkmark-success-status"/> : "" }
                            {errors.oldPasswordError ? <label id="form-error-status">{errors.oldPasswordError}</label> : ""}
                            
                            <input
                            type={showPassword ? "text" : "password"}
                            placeholder="Old password"
                            className="form-control shadow-none"
                            name="oldPassword"
                            value={user.oldPassword}
                            onChange={handleChange}
                            />

                        </div>

                        <div className="mb-2">

                            <Link to='' onClick={toggleConfirmPassword}>
                                {showConfirmPassword ? <IoMdEyeOff className="password-icon" /> : <IoMdEye className="password-icon" />} 
                            </Link>

                            <label>New password</label>
                            
                            {user.newPassword.length >= 6 ? <GoCheck size={15} id="checkmark-success-status"/> :  <label id="form-error-status">{errors.newPasswordError}</label>}
                            <input
                            type={showConfirmPassword ? "text" : "password"}
                            placeholder="New password"
                            className="form-control shadow-none"
                            name="newPassword"
                            value={user.newPassword}
                            onChange={handleChange}
                            />

                        </div>
                                                    

                        <div className="btn-container mt-3">
                            <button className="change-password-btn mb-2">{loading ? <LoadingComponent color={"#fff"} /> : 'Update password'}</button>
                        </div>
                        <ToastContainer/>

                    </div>
                </form>
                
            </div>
        </div>
    )
}

export default ChangePasswordPage
