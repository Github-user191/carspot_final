import React, { useState, useEffect } from 'react'
import BackendService from './services/BackendService';
import Select from 'react-select';
import { customStylesContactSelect, customStylesCreateAd } from './common/CustomSelectStyles';
import axios from 'axios';
import { GoCheck } from 'react-icons/go'
import { Bounce, Slide, toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import { useNavigate } from 'react-router';
import UserService from './services/UserService';
import AuthenticationService from './services/AuthenticationService';
import { IoIosArrowForward } from 'react-icons/io'
import LoadingComponent from './common/LoadingComponent';


const ContactUsPage = () => {

  const theme = (theme) => ({
    ...theme,
    spacing: {
      ...theme.spacing,
      controlHeight: 30,
      baseUnit: 0,

    }
  });

  const [userState, setUserState] = useState({
    isLoggedIn: false,
    isLoggedInText: ""
  });


  const [loading, setLoading] = useState(false);
  let user;

  useEffect(() => {
    user = AuthenticationService.getCurrentUserJwt();
    if (user) {
      setUserState({
        isLoggedIn: true
      })
    }

  }, [])


  const messageTemplates = ["Please delete my account", "I've been scammed and I need help", "I have an idea to improve the website"];

  const options = [
    { value: '1', label: 'Delete my account' },
    { value: '2', label: 'I\'ve been scammed' },
    { value: '3', label: 'Suggest a feature' },
    { value: '4', label: 'Unable to create Ad' },
    { value: '5', label: "Website slow" }
  ]


  const [contactForm, setContactForm] = useState({
    contactFormFullName: "",
    contactFormEmailAddress: "",
    contactFormMessage: "",
    contactFormSubject: ""
  })


  const [errors, setErrors] = useState({
    contactFormFullNameError: "",
    contactFormEmailAddressError: "",
    contactFormSubjectError: "",
    contactFormMessageError: ""
  })

  const handleChange = (event) => {
    const { name, value } = event.target;
    setContactForm(prev => ({
      ...prev, [name]: value
    }))


  }


  const handleSelectChange = (event) => {
    setContactForm(
      {contactFormFullName: contactForm.contactFormFullName,
        contactFormEmailAddress: contactForm.contactFormEmailAddress,
        contactFormSubject: event.label,
      contactFormMessage: contactForm.contactFormMessage}
      )
  }

  const contactFormQuery= (id) => {
    setContactForm({
      contactFormFullName: userState.fullName,
      contactFormEmailAddress: userState.emailAddress,
      contactFormMessage: messageTemplates[id],
      contactFormSubject: options[id].label
    })
  }
 
  const notifySuccess = (message) => {
    toast.success(message);
  }

  const clearInput = () => {
    setContactForm({
      contactFormFullName: "",
      contactFormEmailAddress: "",
      contactFormMessage: "",
      contactFormSubject: ""
    })
  }

  const handleSubmit = (event) => {
    event.preventDefault();

    setLoading(true)
    UserService.sendContactUsEmail(contactForm)
      .then(res => {
        clearInput();
        
        setLoading(false);
        setErrors({
          contactFormFullNameError: "",
          contactFormEmailAddressError: "",
          contactFormSubjectError: "",
          contactFormMessageError: "",
        });
        notifySuccess(res.data)
        

      }).catch(err => {

        setErrors({
          contactFormFullNameError: err.response.data.contactFormFullName,
          contactFormEmailAddressError: err.response.data.contactFormEmailAddress,
          contactFormSubjectError: err.response.data.contactFormSubject,
          contactFormMessageError: err.response.data.contactFormMessage
        });
        setLoading(false);
      })

    if (!userState.isLoggedIn) {
      setUserState({
        isLoggedInText: "You must be logged in to perform that action."
      })
    }
  }

  return (
    <div className="contact-us-page-container">


      <div className="content">
        <div className="contact-us-left-info">

          <form className="contact-us-form" onSubmit={handleSubmit}>
            <div className="header">
              <h3>
                Contact Us
              </h3>
              <p>Want us to add a new feature to our website? Fill out the contact form below</p>

  
            </div>

            <div className="form-group">
              <div className="mb-1">
                {contactForm.contactFormFullName ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.contactFormFullNameError}</label>}
                <label>Full name</label>
                <input
                  type="text"
                  placeholder="Full name"
                  className="form-control shadow-none"
                  name="contactFormFullName"
                  value={contactForm.contactFormFullName}
                  onChange={handleChange}
                />


              </div>

              <div className="mb-1">

    
              {contactForm.contactFormEmailAddress && contactForm.contactFormEmailAddress.includes("@", -1) ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.contactFormEmailAddressError}</label>}
                <label>Email address</label>
                <input
                  type="text"
                  placeholder="Email address"
                  className="form-control shadow-none"
                  name="contactFormEmailAddress"
                  value={contactForm.contactFormEmailAddress}
                  onChange={handleChange}
                />
              </div>

              <div className="mb-1"  >
                <label>Subject</label>
                {contactForm.contactFormSubject ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.contactFormSubjectError}</label>}
                <Select
                  value={{label: contactForm.contactFormSubject}}
              
                  name="contactFormSubject"
                  onChange={handleSelectChange}
                  placeholder="Select subject"
                  options={options}
                  className="contact-select-subject"
                  isSearchable={true}
                  styles={customStylesContactSelect}
                  
                />


              </div>

              <div className="mb-2">
                {contactForm.contactFormMessage && contactForm.contactFormMessage.length >= 20 ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.contactFormMessageError}</label>}
                <label>Message</label>
                <textarea placeholder="Message"
                  className="form-control shadow-none"
                  name="contactFormMessage"
                  value={contactForm.contactFormMessage}
                  onChange={handleChange}
                />


              </div>


              <div className="btn-container mt-3">
                <button className="contact-btn mt-2">{loading ? <LoadingComponent color={"#fff"} /> : 'Send message'}</button>
                <label id="form-error-status">{userState.isLoggedInText}</label>
              </div>


              <ToastContainer />
            </div>

          </form>
        </div>

        <div className="contact-us-right-info">
          <div>

            <div className="header">
              <h2>How can we help?</h2>
              <p>Please select a topic below related to your inquiry.</p>
            </div>




            <div className="query-container" onClick={() => contactFormQuery(0)}>
              <h6 className='mt-4'>I need help deleting my account</h6>
              <p>Select this option if you want to delete your CarSpot account, our team will respond to your query shortly with steps to delete your account.</p>
              <IoIosArrowForward size={20} className="query-arrow-icon" style={{ cursor: "pointer" }} />
              <hr />
            </div>

            <div className="query-container" onClick={() => contactFormQuery(1)}>
              <h6 className='mt-4'>I got scammed</h6>
              <p>Select this option if you were scammed by a user of CarSpot, we will try our best to look into your issue once we have sufficient information.</p>
              <IoIosArrowForward size={20} className="query-arrow-icon" style={{ cursor: "pointer" }} />
              <hr />
            </div>

            <div className="query-container" onClick={() => contactFormQuery(2)}>
              <h6 className='mt-4'>Suggest a feature</h6>
              <p>Select this option if you have suggestions to improve the functionality or to report an issue regarding the website. </p>
              <IoIosArrowForward size={20} className="query-arrow-icon" style={{ cursor: "pointer" }} />
              <hr />
            </div>


          </div>

        </div>
      </div>




    </div>
  );
}

export default ContactUsPage
