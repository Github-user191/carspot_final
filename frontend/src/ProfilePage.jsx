import React, { useEffect, useState } from 'react'
import NavigationBar from './common/NavigationBar'
import MobileNavigationHeader from './MobileNavigationHeader'

import {FiLogOut} from 'react-icons/fi'
import {IoIosArrowForward} from 'react-icons/io'
import UserProfileInfoComponent from './UserProfileInfoComponent'

import defaultAvatar from "./assets/avatar.svg";
import { NavLink, useNavigate } from 'react-router-dom'
import AuthenticationService from './services/AuthenticationService'
import UserService from './services/UserService'
import FooterComponent from './common/FooterComponent'
import PostService from './services/PostService'
import { toast, ToastContainer } from 'react-toastify';

const ProfilePage = () => {

    const [avatar,setAvatar] = useState(null);
    const [userState, setUserState] = useState({
        fullName: '',
        emailAddress: ''
    });

    const[postCount, setPostCount] = useState({
        activePosts: 0,
        allPosts: 0,
        watchlistedPosts: 0
    });

    useEffect(() => {

        PostService.getAllPostsCount()
            .then(res => {
                setPostCount(res.data)
            }).catch(err => {
		setPostCount(0)
            })

        UserService.getUserInfo()
            .then(res => {
                const {id, fullName, emailAddress, totalAds, posts, userAvatar} = res.data;
                setUserState({
                    fullName: fullName,
                    emailAddress: emailAddress,
                    totalAds: totalAds,
                    activePostCount: 0,
                    allPostCount: 0,
                    watchListedPostCount: 0
                });
                
                if(userAvatar) {
                    setAvatar(userAvatar.imageUrl)
                }

            }).catch(err => {
                toast.error("There was an error performing this action. Please try again shortly")
            })

    }, [])

    const navigate = useNavigate();

    const logout = () => {
        AuthenticationService.logout();
        navigate('/login');
      }
    
    
    return (
        <div className="profile-page">
          <MobileNavigationHeader heading="Your Profile"/>
            <div className="content">   
            
         
                <UserProfileInfoComponent 
                    fullName={userState.fullName}
                    yearJoined={2019}
                    defaultAvatar={!avatar && defaultAvatar}
                    avatar={avatar && avatar}
                    activeAdCount={postCount.activePosts}
                    activeAdText="Active posts"
                    totalAdCount={postCount.allPosts}
                    totalAdText="Total posts"
                    watchlistedAdCount={postCount.watchlistedPosts}
                    watchlistedAdText="Watchlisted posts"
                />
            </div>

            <div className="profile-options-container">
                <hr id="main-divider" />
          
                <div className="user-profile-settings-container">

                    <ul class="list-group list-group-flush section">
    
                        <li class="list-group-item">
                            <NavLink to="/" style={{ textDecoration: "none" }}>
                                <p>Home <IoIosArrowForward id="arrow-right-icon" /></p>
                            </NavLink>
                        </li>
                        <li class="list-group-item">
                            <NavLink to="/my-posts" style={{ textDecoration: "none" }}>
                                <p>My posts <IoIosArrowForward id="arrow-right-icon" /></p>
                            </NavLink>
                        </li>

                        <li class="list-group-item">
                            <NavLink to="/change-account-details" style={{ textDecoration: "none" }}>
                                <p>Change account details <IoIosArrowForward id="arrow-right-icon" /></p>
                            </NavLink>
                        </li>

                        <li class="list-group-item">
                            <NavLink to="/change-password" style={{ textDecoration: "none" }}>
                                <p>Change password<IoIosArrowForward id="arrow-right-icon" /></p>
                            </NavLink>
                        </li>



                        <li class="list-group-item" onClick={logout}>
                            <p className="sign-out">Sign out <FiLogOut id="sign-out-icon" color='red' /></p>
                        </li>

                        <li class="list-group-item"></li>
                    </ul>
                </div>
            </div>

   

            <div id="footer-component">
                <FooterComponent />
            </div>
        </div>
    )
}

export default ProfilePage
