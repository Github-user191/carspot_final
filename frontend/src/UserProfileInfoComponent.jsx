import React from 'react'
import { Link } from 'react-router-dom';


const UserProfileInfoComponent = (props) => {
    return (
      <div>
        <div className="user-profile-container">
        {props.defaultAvatar ? 
          <img src={props.defaultAvatar} className="user-profile-avatar" alt="user avatar" /> 
          : 
          <img src={props.avatar} className="user-profile-avatar" alt="user avatar" />
        }


          <div className="user-name-info">
            <h4>{props.fullName}</h4>
            <p>User since {props.yearJoined}</p>
          </div>

          <div className="user-stats-info">
            <div className="active-ads">
                {props.phoneIcon}
                <h4 id="user-profile-heading">{props.activeAdCount}</h4>
                <p>{props.activeAdText}</p>

                <h4 id="user-profile-heading" className='mb-2'>{props.phoneText}</h4>
                <p>{props.phoneInfo}</p>
            </div>

            <div className="total-ads">
                {props.emailIcon}
                <h4 id="user-profile-heading">{props.totalAdCount}</h4>
                <p>{props.totalAdText}</p>

                <h4 id="user-profile-heading" className='mb-2'>{props.emailText}</h4>
                <p>{props.emailInfo}</p>
            </div>

            <div className="watchlisted-ads">
                {props.chatIcon}
                <h4 id="user-profile-heading">{props.watchlistedAdCount}</h4>
                <p>{props.watchlistedAdText}</p>

                <h4 id="user-profile-heading" className='mb-2'>{props.chatText}</h4>
                <p>{props.chatInfo}</p>
            </div>
          </div>
        </div>
      </div>
    );
}

export default UserProfileInfoComponent
