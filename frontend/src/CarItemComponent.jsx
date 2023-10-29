import React from 'react'

import location from "./assets/location.svg"
import time from "./assets/time.svg"
import watchlist from "./assets/add.svg"
import car from "./assets/car-post.svg"

const MAX_LENGTH_TITLE = 20;
const MAX_LENGTH_DESC = 60;

const CarItemComponent = (props) => {

    const addPaddingToDateMonthLessThan10 = (n) => {
        return n < 10 ? '0' + n : n
    }

    let today = new Date();
    const dd = today.getDate();
    const mm = today.getMonth() + 1;
    const yyyy = today.getFullYear();
    today = yyyy + '-' + addPaddingToDateMonthLessThan10(mm) + '-' + dd;


    return (
        <div className="car-item">

            <div className="car-card" >
                <div className="image" onClick={props.viewPostOnClick}>
                    {props.noPreviewImage ? <img src={props.noPreviewImage} alt="car image" /> : <img src={props.imageUrl} alt="car image" />}
                </div>

                <div className="info">
                    <div className="heading" onClick={props.viewPostOnClick}>
                        <h1 className="item-heading">

                            {props.title && (
                                props.title.length > MAX_LENGTH_TITLE ? <p className="item-desc">{`${props.title.substring(0, MAX_LENGTH_TITLE)}...`}</p>
                                    :
                                    <p className="item-desc">{props.title}</p>

                            )}
                        </h1>
                    </div>


                    <div className="price" onClick={props.viewPostOnClick}>
                        <h4 className="item-price">R{props.price}</h4>
                    </div>
                    <div className="desc" onClick={props.viewPostOnClick}>
                        {props.description && (
                            props.description.length > MAX_LENGTH_DESC ? <p className="item-desc">{`${props.description.substring(0, MAX_LENGTH_DESC)}...`}</p>
                                :
                                <p className="item-desc">{props.description}</p>

                        )}
                    </div>

                    <div className="metadata">
                        <div className="location" onClick={props.searchByLocationTag} style={{cursor: "pointer"}}>
                            <img src={location} className="location-icon" alt="Location icon" />
                            <p className="text">{props.location}</p>
                        </div>

                        <div className="time">
                            <img src={time} className="time-icon" alt="Time icon" />
                            <p className="text">{Math.abs(Math.floor((Date.parse(props.time) - Date.parse(today)) / 86400000))} day(s) ago</p>
                        </div>


                        <div className="watchlist" onClick={props.addWatchlistPostOnClick} style={{cursor: "pointer"}}>
                            <img src={watchlist} className="watchlist-icon" alt="Watchlist icon" />
                            <p className="text">Watchlist</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default CarItemComponent
