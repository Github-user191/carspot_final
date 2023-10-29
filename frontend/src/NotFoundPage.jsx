import React from 'react'
import MobileNavigationHeader from './MobileNavigationHeader'
import NotFoundImage from './assets/not-found-illustration.svg'
import FooterComponent from './common/FooterComponent'
import { Link } from 'react-router-dom'
import { LandingPageNavigationBar } from './common/NavigationBar'

const NotFoundPage = () => {
    return (
        <div className="not-found-page">
            {/* <MobileNavigationHeader heading={"Oops,nothing to see here..."} infoDefault={"The link you were looking for no longer exists."}/> */}
            <LandingPageNavigationBar/>
 

            <div className="content">

                <h2>Oops,nothing to see here...</h2>
                <p>The link you were looking for no longer exists.</p>

                <div className="not-found-image-container">
                    <img src={NotFoundImage} alt="Page not found image"/>
                </div>

                <Link to="/">
                    <button>Back to homepage</button>
                </Link>
    

            </div>
            <div id="footer-component">
                <FooterComponent />
            </div>
        </div>
    )
}

export default NotFoundPage
