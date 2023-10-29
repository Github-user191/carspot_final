import React, {useEffect, useState} from "react";
import Hamburger from "hamburger-react";
import {GrAdd} from "react-icons/gr"
import {MdOutlineLogout, MdLibraryAdd, MdLogin, MdLogout} from "react-icons/md"
import {FiLogIn, FiLogOut} from "react-icons/fi"
import defaultAvatar from "../assets/user-profile.svg";
import logoLight from "../assets/logo-light.svg";
import logoDark from "../assets/logo.svg";
import {Link, useNavigate} from "react-router-dom";
import AuthenticationService from "../services/AuthenticationService";
import SearchBarComponent from "./SearchBarComponent";
import UserService from "../services/UserService";
import reactSelect from "react-select";

export const LandingPageNavigationBar = () => {

    const [query, setQuery] = useState("");

    const [avatar, setAvatar] = useState(null);


    const [auth, setAuth] = useState(false);

    const onQueryChange = (e) => {
        setQuery(e.target.value);
    };

    const navigate = useNavigate();

    useEffect(() => {
        const user = AuthenticationService.getCurrentUserJwt();


        if (user) {
            setAuth(true)
            UserService.getUserInfo()
                .then(res => {

                    if (res.data.userAvatar) {

                        setAvatar(res.data.userAvatar.imageUrl)
                    }


                }).catch(err => {
            })
        }


    }, [])

    const logout = () => {
        AuthenticationService.logout();
        navigate('/login');
    }

    let userInfo = "";


    const [navbarExpand, setNavbarExpand] = useState(false);

    const toggleExpand = () => {
        setNavbarExpand(!navbarExpand);
    }
    return (
        <div className="navbar-container">

            <nav className="navbar navbar-expand-lg navbar-landing">
                <div className="container-fluid navbar-content">

                    <button className="shadow-none border-none navbar-toggler navbar-hamburger-icon" type="button"
                            data-bs-toggle="collapse" onClick={toggleExpand} data-bs-target="#navbarNavAltMarkup"
                            aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
                        <Hamburger size={25} direction="right" distance="lg" color="#fff"/>
                    </button>

                    <div className="collapse navbar-collapse navbar-items" id="navbar-landing-items"
                         id="navbarNavAltMarkup">
                        <Link to="/">
                            <img src={logoLight} className="navbar-logo-dark" alt="Logo image"/>
                        </Link>

                        <ul className="navbar-nav me-auto mb-2 mb-lg-0">

                            <li className="nav-item">
                                <Link to="/" className="nav-link">Home</Link>
                            </li>

                            <li className="nav-item">
                                <Link to="/frequently-asked-questions" className="nav-link">FAQs</Link>
                            </li>
    
                            <li className="nav-item">
                                <Link to="/user-reviews" className="nav-link">Reviews</Link>
                            </li>

                            <li className="nav-item">
                                <Link to="/safety-tips" className="nav-link">Safety</Link>
                            </li>
                            
                            {auth && (
                                <Link to="/create">
                                    {/* <li className="nav-item"> <a className="nav-link" href="/safety-tips">Create Ad</a> </li> */}
                                    <button type="button" className="post-ad-btn shadow-none">
                                        <MdLibraryAdd fill="white" color="white" size="15"
                                                      style={{marginRight: ".15rem"}}/> Sell your car
                                    </button>
                                </Link>
                            )}

                            {/* <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                  Dropdown
                </a>
                <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                  <li><a class="dropdown-item" href="#">Action</a></li>
                  <li><a class="dropdown-item" href="#">Another action</a></li>
                  <li><hr class="dropdown-divider"/></li>
                  <li><a class="dropdown-item" href="#">Something else here</a></li>
                </ul>
              </li> */}
                        </ul>


                        <SearchBarComponent
                            name={"query"}
                            value={query}
                            formClass={"navbar-search-form"}
                            formContainerClass={"navbar-search-form-container"}
                            searchIconSize={15}
                            onChange={onQueryChange}
                            searchIconStyle={{marginTop: "-.7rem"}}
                            inputStyle={{height: "40px"}}

                        />


                        <div className="navbar-btn-container row">

                            {!auth &&
                                <div className="col">
                                    <Link to="/register">
                                        <button type="button" className="navbar-register-btn shadow-none">
                                            Sign up
                                        </button>
                                    </Link>
                                </div>
                            }


                            <div className="col">
                                {auth ?
                                    <button type="button" className="navbar-signout-signin-btn shadow-none"
                                            onClick={logout}>
                                        <MdLogout size={16} fill="#fff" style={{marginRight: ".35rem"}}/> Logout
                                    </button>

                                    :
                                    <Link to="/login">
                                        <button type="button" className="navbar-signout-signin-btn shadow-none">
                                            <MdLogin size={16} fill="#fff" style={{marginRight: ".35rem"}}/> Login
                                        </button>
                                    </Link>
                                }
                            </div>
                        </div>


                    </div>

                    <div className="navbar-right">
                        {auth && (
                            !navbarExpand &&
                            <Link to="/profile">
                                <img src={avatar ? avatar : defaultAvatar} alt="user avatar"
                                     className="user-avatar-img"/>
                            </Link>
                        )}

                    </div>
                </div>
            </nav>

        </div>
    )

}