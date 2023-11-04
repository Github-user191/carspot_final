import React from 'react'
import {FiUsers} from 'react-icons/fi'
import {FaRegSmile} from 'react-icons/fa'
import {VscWorkspaceTrusted} from 'react-icons/vsc'
import {AiOutlineStar} from 'react-icons/ai'

const LandingPageStatisticsComponent = () => {
    return (
      <div className="landing-statistics-page">

        <div className="content">
      
          <div className='statistic-container row'>
            <div className='col-md'>
              <div className='statistic'>
                <FaRegSmile className="statistic-icon" size={30}/>
                <h4>Plenty of satisfied users</h4>
                <p>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit ut aliquam, purus sit amet luctus venenatis, lectus magna fringilla urna.
                </p>
              </div>
            </div>
         

            <div className='col'>
              <div className='statistic'>
                <VscWorkspaceTrusted className="statistic-icon" size={30}/>
                <h4>Trusted marketplace</h4>
                <p>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit ut aliquam, purus sit amet luctus venenatis, lectus magna fringilla urna.
                </p>
              </div>
            </div>

            <div className='col'>
              <div className='statistic'>
                <AiOutlineStar className="statistic-icon" size={30}/>
                <h4>100+ 5 star reviews</h4>
                <p>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit ut aliquam, purus sit amet luctus venenatis, lectus magna fringilla urna.
                </p>
              </div>
            </div>
          </div>


        </div>
      </div>
    );
}

export default LandingPageStatisticsComponent
