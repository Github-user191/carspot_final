import React from 'react'
import {BsTwitter} from 'react-icons/bs'
import { Link, NavLink } from 'react-router-dom'
import logoLight from "../assets/logo-light.svg"

const FooterComponent = () => {
    return (
    
  <div className="my5">


    <footer className="text-center text-lg-start footer-container">

      <section className="footer-body">
        <div className="text-center text-md-start mt-5">
          <div className="row mt-3">
  
            <div className="col-md-3 col-lg-4 col-xl-3 mx-auto mb-4 company-section footer-column-section">

              <h6 className="text-uppercaswe fw-bwold">Carspot</h6>

              {/* <img src={logoLight} className="logo-icon" alt="Logo image" /> */}
              <hr id="text-underline-accent" />
              <p>
                Incididunt velit mollit aliqua culpa elit do elit esse veniam ullamco et. Tempor ut in anim dolor dolore velit. Sit.
              </p>
            </div>

            <div className="col-md-2 col-lg-2 col-xl-2 mx-auto mb-4 products-section footer-column-section">
      
              <h6 className="text-uppercaswe fw-bwold">About Us</h6>
              <hr id="text-underline-accent" />
              
  
                <p style={{wordWrap: "break-word"}}> Incididunt velit mollit aliqua culpa elit do elit esse veniam ullamco et. Tempor ut in anim dolor dolore velit. Sit. </p>



            </div>

            <div className="col-md-3 col-lg-2 col-xl-2 mx-auto mb-4 useful-links-section footer-column-section">

              <h6 className="text-uppercaswe fw-bwold">FAQ</h6>
              <hr id="text-underline-accent" />

              <Link to="/frequently-asked-questions" className='footer-links'>
                <p> Why did my post disappear? </p>
              </Link>

              <Link to="/frequently-asked-questions" className='footer-links'>
                <p> How do i request for a post to be removed? </p>
              </Link>

              <Link to="/frequently-asked-questions" className='footer-links'>
                <p> I was scammed, how can I prevent this from happening to other users? </p>
              </Link>

              <Link to="/frequently-asked-questions" className='footer-links'>
                <p> How can i delete my CarSpot account </p>
              </Link>

            </div>

            <div className="col-md-3 col-lg-2 col-xl-2 mx-auto mb-4 contact-section footer-column-section">
  
              <h6 className="text-uppercaswe fw-bwold">Contact</h6>
              <hr id="text-underline-accent" />
              <p>contact@carspot.com</p>
              <p>+27 73 388 0232</p>
            </div>
        
          </div>

        </div>
      </section>

    </footer>


</div>
    )
}

export default FooterComponent

