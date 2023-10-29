import React, {useEffect, useState} from 'react'
import { Link } from 'react-router-dom'
import AuthenticationService from './services/AuthenticationService'
import axios from 'axios';
import LoadingComponent from './common/LoadingComponent';
import { Bounce, Slide, toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';


const ForgotPasswordFormModal = () => {


    const [user, setUser] = useState({
        forgotPasswordEmail: ''
    })

    const [errors, setErrors] = useState({
        forgotPasswordEmailError: ''
    })

    const [loading, setLoading] = useState(false);

    const notifySuccess = (message) => {
        toast.success(message);
    }

    
    const handleChange = (event) => {
        const {name, value} = event.target;

        setUser(prev => ({
            ...prev, [name]:value
        }))
    }
    
    const handleForgotPasswordSubmit = (event) => {
        event.preventDefault();
        
        console.log(user.emailAddress)
        setErrors({
            forgotPasswordEmailError: ''
        })
        
        if(user.forgotPasswordEmail) {
            setLoading(true);
        }


        AuthenticationService.forgotPassword(user)
            .then(res => {
            setLoading(false)
            setUser({forgotPasswordEmail: ''})


            notifySuccess(res.data.message)
        }).catch(err => {
            setLoading(false)
            setErrors({
                forgotPasswordEmailError: "Account does not exist"
            })

        })

        
    }
        



    return (
        <div className='forgot-password-modal-component'>
            
                <Link to="/forgot-password" data-bs-toggle="modal" data-bs-target="#staticBackdrop">
                    <p>Forgot your password?</p>
                </Link>

     
                <div className="modal fade forgot-password-modal" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                    <div className="modal-dialog modal-dialog-centered">
                        <div className="modal-content">
                            <div className="modal-header">
            
                                <h5 className="modal-title" id="staticBackdropLabel">Forgot your password?</h5>

                            
                                <button type="button" className="btn-close shadow-none" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
        
                            <div className="modal-body">
                                <form className="forgot-password-modal-form" >

                                    <div className="form-group">

                                        <div className="mb-3">
            
          
                                            <label className="form-label">
                                                Enter your email address 
                                                {errors.forgotPasswordEmailError ? <label id="form-error-status">{errors.forgotPasswordEmailError}</label> : ''}
                                            </label>

                                            <input
                                            type="text"
                                            placeholder="Email address"
                                            className="form-control shadow-none"
                                            name="forgotPasswordEmail"

                                            value={user.forgotPasswordEmail}
                                            onChange={handleChange}
                                            />

                                        </div>
                                    </div>

                                    <button className='mt-2' onClick={handleForgotPasswordSubmit}>
                                    {loading ? <LoadingComponent color={"#fff"}/> : 'Send' }           
                                    </button>
                         
                                </form>
            
                            </div>
                          
                                {/* <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button> */}
                      
                            
                        </div>
                    </div>
                    <ToastContainer style={{marginTop: "5rem"}}/>
                </div>


        </div>
    )
}

export default ForgotPasswordFormModal
