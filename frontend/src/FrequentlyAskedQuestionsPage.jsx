import React, { useState } from 'react'
import MobileNavigationHeader from './MobileNavigationHeader'
import { BiPlus, BiMinus } from 'react-icons/bi'
import FooterComponent from './common/FooterComponent';

const FrequentlyAskedQuestionsPage = () => {

    const [accordionIconOne, setAccordionIconOne] = useState(false);
    const [accordionIconTwo, setAccordionIconTwo] = useState(false);
    const [accordionIconThree, setAccordionIconThree] = useState(false);
    const [accordionIconFour, setAccordionIconFour] = useState(false);

    return (
        <div className="frequently-asked-questions-page">
            <MobileNavigationHeader heading="Frequently Asked Questions" infoDefault="Can’t find an answer? Send us an email at " infoColored="contact@carspot.com"/>

            <div className="content">
                
                <div className="faq-container">

                    <div class="accordion accordion-flush" id="accordionPanelsStayOpenExample">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="panelsStayOpen-headingOne">
                                <button onClick={() => setAccordionIconOne(!accordionIconOne)} class="accordion-button faq-heading shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-collapseOne" aria-expanded="true" aria-controls="panelsStayOpen-collapseOne">
                                    <span className="accordion-icon-span">{accordionIconOne ?  <BiMinus size={20} className='accordion-icon'/> : <BiPlus size={20} className='accordion-icon'/>}</span>
                                    Why did my post dissapear?
                                </button>
                            </h2>
                            
                            <div id="panelsStayOpen-collapseOne" class="accordion-collapse collapse" aria-labelledby="panelsStayOpen-headingOne">
                                <div class="accordion-body category-body">
            
                                    <p className="faq-answer">
                                        All posts that are created have a default duration of 2 month(s) in which they will be active. After this period, your post will expire and will not be visible for other users to view.
                                        If your post has not been sold by then, feel free to re-create it.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="accordion accordion-flush mt-5" id="accordionPanelsStayOpenExample">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="panelsStayOpen-headingTwo">
                                <button onClick={() => setAccordionIconTwo(!accordionIconTwo)} class="accordion-button faq-heading shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-collapseTwo" aria-expanded="true" aria-controls="panelsStayOpen-collapseTwo">
                                    <span className="accordion-icon-span">{accordionIconTwo?  <BiMinus size={20} className='accordion-icon'/> : <BiPlus size={20} className='accordion-icon'/>}</span>
                                     How can i request for a post to be removed?
                                </button>
                            </h2>
                            
                            <div id="panelsStayOpen-collapseTwo" class="accordion-collapse collapse" aria-labelledby="panelsStayOpen-headingTwo">
                                <div class="accordion-body category-body">
            
                                    <p className="faq-answer">
                                         If you feel that a specific post is inapproriate you can send us an email to <span style={{color: "var(--primary-dark-blue)", fontWeight: "bolder"}}>contact@carspot.com</span> and we will review the post. If we feel that the post does not abide by the rules, we will remove it.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="accordion accordion-flush mt-5" id="accordionPanelsStayOpenExample">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="panelsStayOpen-headingThree">
                                <button onClick={() => setAccordionIconThree(!accordionIconThree)} class="accordion-button faq-heading shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-collapseThree" aria-expanded="true" aria-controls="panelsStayOpen-collapseThree">
                                    <span className="accordion-icon-span">{accordionIconThree ?  <BiMinus size={20} className='accordion-icon'/> : <BiPlus size={20} className='accordion-icon'/>}</span>
                                     I was scammed, how can I prevent this happening to other users?
                                </button>
                            </h2>
                            
                            <div id="panelsStayOpen-collapseThree" class="accordion-collapse collapse" aria-labelledby="panelsStayOpen-headingThree">
                                <div class="accordion-body category-body">
            
                                    <p className="faq-answer">
                                        If you were scammed by a user please send us an email with all the appropriate proof regarding the transaction to <span style={{color: "var(--primary-dark-blue)", fontWeight: "bolder"}}>contact@carspot.com</span>, in the meantime we will attempt to make other users aware of potential scams that are going around and what to be aware of when looking to purchase a vehicle.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="accordion accordion-flush mt-5" id="accordionPanelsStayOpenExample">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="panelsStayOpen-headingFour">
                                <button onClick={() => setAccordionIconFour(!accordionIconFour)} class="accordion-button faq-heading shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-collapseFour" aria-expanded="true" aria-controls="panelsStayOpen-collapseFour">
                                    <span className="accordion-icon-span">{accordionIconFour ?  <BiMinus size={20} className='accordion-icon'/> : <BiPlus size={20} className='accordion-icon'/>}</span>
                                     How can I delete my CarSpot account?
                                </button>
                            </h2>
                            
                            <div id="panelsStayOpen-collapseFour" class="accordion-collapse collapse" aria-labelledby="panelsStayOpen-headingFour">
                                <div class="accordion-body category-body">
            
                                    <p className="faq-answer">
                                        If you feel that you do not need your CarSpot account anymore, you can request a deletion of your account to <span style={{color: "var(--primary-dark-blue)", fontWeight: "bolder"}}>contact@carspot.com</span>, keep in mind that this will remove all of your previous posts and you will not be able to recover them once deleted.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>


                    

                    
                </div>

            </div>

            <div id="footer-component">
                <FooterComponent />
            </div>
        </div>
    )
}

export default FrequentlyAskedQuestionsPage
