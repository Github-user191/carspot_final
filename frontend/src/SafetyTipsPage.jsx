import React, { useState } from 'react'
import MobileNavigationHeader from './MobileNavigationHeader'
import { BiPlus, BiMinus } from 'react-icons/bi'
import FooterComponent from './common/FooterComponent';

const SafetyTipsPage = () => {

    const [accordionIconOne, setAccordionIconOne] = useState(false);
    const [accordionIconTwo, setAccordionIconTwo] = useState(false);
    const [accordionIconThree, setAccordionIconThree] = useState(false);

    const toggleAccordionIconChangeOne = () => {
        setAccordionIconOne(!accordionIconOne);
    }

    const toggleAccordionIconChangeTwo = () => {
        setAccordionIconTwo(!accordionIconTwo);
    }

    const toggleAccordionIconChangeThree = () => {
        setAccordionIconThree(!accordionIconThree);
    }
    return (
        <div className="safety-tips-page">
            <MobileNavigationHeader heading="Safety tips" infoDefault="It is of upmost importance that you are aware of these tips provided to assist in 
            preventing scams and more"/>

            <div className="content">
                
                <div className="safety-tips-container">

                    <div class="accordion accordion-flush" id="accordionPanelsStayOpenExample">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="panelsStayOpen-headingOne">
                                <button onClick={toggleAccordionIconChangeOne} class="accordion-button faq-heading shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-collapseOne" aria-expanded="true" aria-controls="panelsStayOpen-collapseOne">
                                    <span className="accordion-icon-span">{accordionIconOne ?  <BiMinus size={20} className='accordion-icon'/> : <BiPlus size={20} className='accordion-icon'/>}</span>
                                     Never send or wire money to sellers or buyers.
                                </button>
                            </h2>
                            
                            <div id="panelsStayOpen-collapseOne" class="accordion-collapse collapse" aria-labelledby="panelsStayOpen-headingOne">
                                <div class="accordion-body category-body">
            
                                    <p className="faq-answer">
                                    Lorem ipsum dolor sit amet, consectetur adipiscing elit ut aliquam, purus sit amet luctus venenatis, lectus magna fringilla urna, porttitor rhoncus dolor purus non enim praesent elementum facilisis leo.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="accordion accordion-flush mt-5" id="accordionPanelsStayOpenExample">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="panelsStayOpen-headingTwo">
                                <button onClick={toggleAccordionIconChangeTwo} class="accordion-button faq-heading shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-collapseTwo" aria-expanded="true" aria-controls="panelsStayOpen-collapseTwo">
                                    <span className="accordion-icon-span">{accordionIconTwo?  <BiMinus size={20} className='accordion-icon'/> : <BiPlus size={20} className='accordion-icon'/>}</span> 
                                    Meet in a public space to see the item and exchange money.
                                </button>
                            </h2>
                            
                            <div id="panelsStayOpen-collapseTwo" class="accordion-collapse collapse" aria-labelledby="panelsStayOpen-headingTwo">
                                <div class="accordion-body category-body">
            
                                    <p className="faq-answer">
                                    Lorem ipsum dolor sit amet, consectetur adipiscing elit ut aliquam, purus sit amet luctus venenatis, lectus magna fringilla urna, porttitor rhoncus dolor purus non enim praesent elementum facilisis leo.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="accordion accordion-flush mt-5" id="accordionPanelsStayOpenExample">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="panelsStayOpen-headingThree">
                                <button onClick={toggleAccordionIconChangeThree} class="accordion-button faq-heading shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-collapseThree" aria-expanded="true" aria-controls="panelsStayOpen-collapseThree">
                                    <span className="accordion-icon-span">{accordionIconThree ?  <BiMinus size={20} className='accordion-icon'/> : <BiPlus size={20} className='accordion-icon'/>}</span>
                                    Never send your item before receiving the money.
                                </button>
                            </h2>
                            
                            <div id="panelsStayOpen-collapseThree" class="accordion-collapse collapse" aria-labelledby="panelsStayOpen-headingThree">
                                <div class="accordion-body category-body">
            
                                    <p className="faq-answer">
                                    Lorem ipsum dolor sit amet, consectetur adipiscing elit ut aliquam, purus sit amet luctus venenatis, lectus magna fringilla urna, porttitor rhoncus dolor purus non enim praesent elementum facilisis leo.
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

export default SafetyTipsPage
