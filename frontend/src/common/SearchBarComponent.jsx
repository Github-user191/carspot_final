import React from 'react';
import {BiSearchAlt2} from "react-icons/bi"
import { Link, useNavigate, useSearchParams} from 'react-router-dom';



const SearchBarComponent = ({inputStyle, searchIconSize, onChange, formClass, name, value, onClick, formContainerClass}) => {

  const [searchParams] = useSearchParams();
  const currentQuery = searchParams.get("query");
  const navigate = useNavigate();


  
  const handleOnSubmit = (e) => {
    e.preventDefault()
    const formData = new FormData(e.currentTarget);
    const query = formData.get("query");
    // if (!query) return;
    navigate({ pathname: "/car-list", search: `?query=${query}`});
  };

  
  return (
    <div className={formContainerClass}>
      <form className={formClass} onSubmit={handleOnSubmit}>
          <div class="form-group">
              <input
                type="text"
                name={name}
                value={value}
                onChange={onChange}
                defaultValue={currentQuery}
                className="form-control shadow-none"
                placeholder="Search your dream car.."
                style={inputStyle}
              />
    
              <button type="submit" onClick={onClick}>
                <BiSearchAlt2 className="search-icon" size={searchIconSize} />
              </button>
          </div>
      </form>
    </div>

  )
};

export default SearchBarComponent;
