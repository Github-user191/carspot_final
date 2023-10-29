import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import ForgotPasswordFormModal from '../ForgotPasswordFormModal'

const FormComponent = () => {


    return (
        <form className="login-form">

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
                        {/* {errors.emailAddressError ? <label id="form-error-status">{errors.emailAddressError}</label> : ''} */}
                        <input type="text" placeholder="Email address" className="form-control shadow-none"/>
                    </div>

                    <div className="mb-1">
                        <label> Email Address </label>
                        {/* {errors.emailAddressError ? <label id="form-error-status">{errors.emailAddressError}</label> : ''} */}
                        <input type="text" placeholder="Email address" className="form-control shadow-none"/>
                    </div>

                    <div className="mb-1">
                        <label> Email Address </label>
                        {/* {errors.emailAddressError ? <label id="form-error-status">{errors.emailAddressError}</label> : ''} */}
                        <input type="text" placeholder="Email address" className="form-control shadow-none"/>
                    </div>

                    <div className='btn-container'>

                         <ForgotPasswordFormModal />
                        <button>
                            Login
                        </button>
                    </div>
                </div>
      



        </form>
    )

}

export default FormComponent