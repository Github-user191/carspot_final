import React from 'react'
import LandingPage from './LandingPage'
import {LandingPageNavigationBar} from './common/NavigationBar';
import LandingPageStatisticsComponent from './LandingPageStatisticsComponent'
import ContactUs from './ContactUsPage'
import FooterComponent from './common/FooterComponent'

const LandingPageMainComponent = () => {
    return (
        <div>
            <LandingPage />
            <LandingPageStatisticsComponent />           
            <ContactUs />
            <FooterComponent />
    
        </div>
    )
}

export default LandingPageMainComponent
