import React, {useState, useEffect} from 'react'
import emailConfirmedImage from "./assets/email-confirmed.svg"
import { LandingPageNavigationBar } from './common/NavigationBar';
import axios from 'axios';
import {useParams } from 'react-router';
import { Link } from 'react-router-dom';
import AuthenticationService from './services/AuthenticationService';
import { Bounce, Slide, toast, ToastContainer } from "react-toastify";
import LoadingComponent from './common/LoadingComponent';

const EmailConfirmedPage = () => {
    

    const [loading, setLoading] = useState(false);

    const [accountVerifiedStatus, setAccountVerifiedStatus] = useState("");
    const [accountVerified, setAccountVerified] = useState(false);


    const {token} = useParams();

    const resendConfirmationEmail = () => {
        setLoading(true)
        AuthenticationService.resendConfirmationEmail(token)
            .then(res=> {
                toast.success(res.data.message)
                setLoading(false)
            }).catch(err => {
                setLoading(false)
                toast.warn(err.response.data.invalidToken);
            })
    }
    useEffect(() => {
        AuthenticationService.confirmEmail(token)
        .then((res) => {
            setAccountVerified(true)
            setAccountVerifiedStatus(res.data.message)
        })
        .catch((err) => {
            setAccountVerified(false);

            err.response.data.message ? setAccountVerifiedStatus(err.response.data.message) : setAccountVerifiedStatus(err.response.data.invalidToken);
 
        });
    
    }, []);

    return (
        <div className="email-confirmed-page">
            <LandingPageNavigationBar/>
    
            <div className="content">

                
                <img src={emailConfirmedImage} alt="Email image"/>

                {<h2>{accountVerifiedStatus}</h2>}

                <div className="btn-container">
                    <Link to="/login">
                        <button className="proceed-login-btn">Proceed to login</button>
                    </Link>

                    <button className="resend-confirmation-btn" onClick={resendConfirmationEmail}>{loading ? <LoadingComponent color={"#fff"} /> : 'Resend email'}</button>
                </div>
         

            </div>
            <ToastContainer />
        </div>
    )
}

export default EmailConfirmedPage
