import React from 'react'
import { LandingPageNavigationBar } from './common/NavigationBar';
import {IoIosArrowBack} from 'react-icons/io'
import { useNavigate } from 'react-router'
import { Link } from 'react-router-dom';

const MobileNavigationHeader = ({heading, infoDefault, infoDefault2, infoColored, link}) => {

    return (

        
        <div>
            <LandingPageNavigationBar/>
            <div className="navigation-page">
                
                <div className="content">
                    
                    <div className="header">
                        <h1 className="heading">{heading}</h1>
                    </div>

                    <div className="sub-header">

                        <p className="info">
                            {infoDefault}
                            <Link to={link} style={{textDecoration: "none"}}>
                                <span style={{ color: "var(--primary-light-blue)", fontWeight: "bold"}}>{infoColored}</span>    
                            </Link> 
                            {infoDefault2}
                        </p>
                    </div>
                   
     
                </div>
                
            </div>
        </div>
       
    )
}

export default MobileNavigationHeader
