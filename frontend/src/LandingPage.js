import React, { useState } from 'react';
import {BiPlus, BiSearchAlt2} from "react-icons/bi"
import { Link, useNavigate } from 'react-router-dom';
import Pagination from './common/PaginationComponent';
import carImage1 from "./assets/car.svg";
import carImage2 from "./assets/car-2.svg";
import { LandingPageNavigationBar } from './common/NavigationBar';
import SearchBarComponent from './common/SearchBarComponent';
import PostService from './services/PostService';
import IconButton from '@mui/material/IconButton';
import Stack from '@mui/material/Stack';


const Landing = () => {
  const navigate = useNavigate();
  const [query, setQuery] = useState("");
  const [error, setError] = useState({
    postNotFoundError: ""
  });



  const onQueryChange = (e) => {
    setQuery(e.target.value);
  };




  return (
    <div className="landing-page">

      <div className="home-container pb-1">
        <LandingPageNavigationBar />

        <div className="content">
          <div className="header">
            {/* <h1 className="heading">Find your <span style={{color: "#fff"}}>dream car</span></h1> */}
            <h1 className="heading">
		Your second-hand marketplace for motor vehicles
            </h1>
            <p className="info mt-3">
              Lorem ipsum dolor sit amet, consectetur adipiscing elit ut aliquam,
              purus sit amet luctus.
            </p>

    
            <SearchBarComponent 
              name={"query"} 
              
              value={query} 
              formClass={"landing-search-form"} 
              onChange={onQueryChange}
              searchIconSize={22} 
            />
          </div>

        </div>
      </div>

      <div className='landing-image-container'>
      <img src={carImage1} className="car-image-1" alt="car images" />
      <img src={carImage2} className="car-image-2" alt="car images" />
      </div>
      
          



     
    </div>
  );
};

export default Landing;
