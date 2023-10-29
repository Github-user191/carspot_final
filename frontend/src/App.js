import React from 'react'
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import './App.css';
import LandingPage from './LandingPage';
import HomeComponent from './LandingPageMainComponent';
import RegisterPage from './common/RegisterPage';
import FooterComponent from './common/FooterComponent';
import MobileNavigationHeader from './MobileNavigationHeader'
import CarListPage from './CarListPage'
import RangeInputComponent from './RangeInputComponent';
import PaginationComponent from './common/PaginationComponent';
import FrequentlyAskedQuestionsPage from './FrequentlyAskedQuestionsPage';
import CreateAdPage from './CreateAdPage';
import ProfilePage from './ProfilePage';
import UserPostsPage from './UserPostsPage';
import CarInfoPage from './CarInfoPage';
import ChangeAccountDetailsPage from './ChangeAccountDetailsPage';
import SafetyTipsPage from './SafetyTipsPage';
import EditAdPage from './EditAdPage';
import UserReviewsPage from './UserReviewsPage';
import Login from './Login';
import NotFoundPage from './NotFoundPage';
import EmailConfirmedPage from './EmailConfirmedPage';
import OAuth2RedirectHandler from './oauth2/OAuth2RedirectHandler';
import ForgotPasswordFormModal from './ForgotPasswordFormModal';
import LoadingComponent from './common/LoadingComponent';
import AuthenticationService from './services/AuthenticationService';
import jwtDecode from 'jwt-decode';
import SecuredRoute from './common/SecuredRoute';
import SkeletonCarCard from './skeleton/SkeletonCarCard';
import ResetPasswordForm from './common/ResetPasswordForm';
import ChangePasswordPage from './ChangePasswordPage';
import PostSuccessModal from './PostSuccessModal';

function App() {

    const userJwt = AuthenticationService.getCurrentUserJwt().jwt;


    if (userJwt) {
        const decodedToken = jwtDecode(userJwt);
        const currentTime = Date.now() / 1000;

        if (decodedToken.exp < currentTime) {
            AuthenticationService.logout();
            window.location.reload();
            window.location.href = "/register"
        }
    }

    return (
        <div className="App">
            <Router>
                <Routes>
                    <Route path="/profile" element={<SecuredRoute><ProfilePage/></SecuredRoute>}/>
                    <Route path="/range" element={<RangeInputComponent/>}/>
                    <Route path="/" element={<HomeComponent/>}/>
                    <Route path="/register" element={<RegisterPage/>}/>
                    <Route path="/footer" element={<FooterComponent/>}/>
                    <Route path="/post-success" element={<PostSuccessModal/>}/>
                    <Route path="/car-list" element={<CarListPage/>}/>
                    <Route path="/car-info" element={<CarInfoPage/>}/>
                    <Route path="/car-info/:id" element={<CarInfoPage/>}/>
                    <Route path="/create" element={<SecuredRoute><CreateAdPage/></SecuredRoute>}/>
                    <Route path="/profile" element={<ProfilePage/>}/>
                    <Route path="/my-posts" element={<SecuredRoute><UserPostsPage/></SecuredRoute>}/>
                    <Route path="/frequently-asked-questions" element={<FrequentlyAskedQuestionsPage/>}/>
                    <Route path="/change-account-details"
                           element={<SecuredRoute><ChangeAccountDetailsPage/></SecuredRoute>}/>
                    <Route path="/safety-tips" element={<SafetyTipsPage/>}/>
                    <Route path="/edit-ad/:id" element={<SecuredRoute><EditAdPage/></SecuredRoute>}/>
                    <Route path="/user-reviews" element={<UserReviewsPage/>}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/change-password" element={<SecuredRoute><ChangePasswordPage/></SecuredRoute>}/>
                    <Route path="/confirm-email/:token" element={<EmailConfirmedPage/>}/>
                    <Route path="/reset-password/:token" element={<ResetPasswordForm/>}/>
                    <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler/>}/>
                    <Route path="/*" element={<NotFoundPage/>}/>

                </Routes>
            </Router>
        </div>
    );
}

export default App;
