import React, { useState, useEffect } from 'react'
import MobileNavigationHeader from './MobileNavigationHeader'
import defaultAvatar from './assets/avatar.svg'
import UserService from './services/UserService';
import PostService from './services/PostService';
import PhoneInput from 'react-phone-input-2'
import 'react-phone-input-2/lib/style.css'
import { GoCheck } from 'react-icons/go'
import FooterComponent from './common/FooterComponent';
import { Bounce, Slide, toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import avi from './assets/avatar.svg'
import AuthenticationService from './services/AuthenticationService';
import { useNavigate } from 'react-router';

import LoadingComponent from './common/LoadingComponent';

const ChangeAccountDetailsPage = () => {

  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const [user, setUser] = useState({
    id: "",
    fullName: "",
    emailAddress: "",
    mobileNumber: "",
  })

  const [avatar, setAvatar] = useState(null);
  const [selectedAvatar, setSelectedAvatar] = useState(null);
  const [previewAvatar, setPreviewAvatar]= useState(null);

  const [errors, setErrors] = useState({
    fullNameError: '',
    emailAddressError: '',
    mobileNumberError: '',
    avatarError: '',
    fileUploadError: ''
  })


  const handleChange = (event) => {
    const { name, value } = event.target;

    setUser(prev => ({
      ...prev, [name]: value
    }))
  }

  const clearInput = () => {
    setUser({
      fullName: "",
      emailAddress: "",
      mobileNumber: ""
    });
  }


  const handlePhoneInputChange = (event) => {
    setUser(prev => ({
      ...prev, mobileNumber: event
    }))
  }

  const handleSubmit = (event) => {
    event.preventDefault();

    setLoading(true)
    UserService.updateUserInfo(user, selectedAvatar)
      .then(res => {
        setUser(res.data)
        if(user.emailAddress !== AuthenticationService.getCurrentUserSubject()){
          toast.success("Success! You will be redirected to login shortly.")
          AuthenticationService.logout();


          navigate("/login")
        }

        toast.success("Success! Your details were changed successfully.")
        setLoading(false)
      }).catch(err => {
        const {fullName, emailAddress, mobileNumber, fileUploadError} = err.response.data;
        setErrors({
          fullNameError: fullName,
          emailAddressError: emailAddress,
          mobileNumberError: mobileNumber,
          fileUploadError: fileUploadError
        })

        if(fileUploadError) {
          toast.warn(fileUploadError)
          setPreviewAvatar(defaultAvatar)
        
        }
      }).finally(() => setLoading(false))
  }

  const onAvatarChange = (event) => {
    const avatar = event.target.files[0]
    if (avatar) {
      setPreviewAvatar(URL.createObjectURL(avatar))
      setSelectedAvatar(avatar)
    }
   }

  useEffect(() => {
    UserService.getUserInfo()
      .then(res => {
        const user = res.data;
    

        setUser({
          id: user.id,
          fullName: user.fullName,
          emailAddress: user.emailAddress,
          mobileNumber: user.mobileNumber
        })

        if(res.data.userAvatar) {
          setAvatar(user.userAvatar.imageUrl)
        }

      }).catch(err => {
      })
  }, [])
  return (
    <div className="change-acount-details-page">
      <MobileNavigationHeader heading={"Account details"} />
      <div className="content">

        <form className="change-account-details-form" onSubmit={handleSubmit}>

          <div className="user-image-container">


            {!selectedAvatar && !avatar ?
            <img src={avi} alt="user avatar" className="user-avatar-img" />  
     
            :
            <img src={selectedAvatar ? previewAvatar : avatar} alt="user avatar" className="user-avatar-img" />
          }
      
             
              <br/>
              <label for="myfile">Change</label>
              <input type="file" id="myfile" class="change-avatar-input" onChange={onAvatarChange}  />
       

          </div>

          <div className="form-group">
            <div className="mb-2">
              <label>Full name</label>

              {user.fullName ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.fullNameError}</label>}
              <input
                type="text"
                placeholder="Full name"
                className="form-control shadow-none"
                name="fullName"
                value={user.fullName}
                onChange={handleChange}
              />
            </div>

            <div className="mb-2">
              <label>Email address</label>
              {user.emailAddress.length > 8 && user.emailAddress.includes("@", -1) ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.emailAddressError}</label>}
              <input
                type="text"
                placeholder="Email address"
                className="form-control shadow-none"
                name="emailAddress"
                value={user.emailAddress}
                onChange={handleChange}
              />
            </div>

            <div className="mb-2">


              <label>Mobile number</label>
              {user.mobileNumber.length > 10 ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.mobileNumberError}</label>}

              <PhoneInput
                buttonStyle={{ backgroundColor: "transparent", border: "none" }}
                buttonClass="mobile-number-btn"
                country={'za'}
                disableDropdown
                inputClass="form-control mobile-number-btn shadow-none"
                inputStyle={{ width: "100%" }}
                masks={{ za: '.. ... ....' }}
                value={user.mobileNumber}
                name="mobileNumber"
                onChange={handlePhoneInputChange}
                countryCodeEditable={false}
                placeholder='00 000 0000'
              />
            </div>



            <div className="btn-container mt-4">
              <button className='update-btn '>{loading ? <LoadingComponent color={"#fff"} /> : 'Update'}</button>
            </div>

          </div>
        </form>

        <ToastContainer />
      </div>
      <div id="footer-component">
        <FooterComponent />
      </div>
    </div>
  );
}

export default ChangeAccountDetailsPage
