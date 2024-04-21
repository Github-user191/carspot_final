import React, { useState, useEffect } from 'react'
import {VscFilter} from "react-icons/vsc";
import {AiOutlineSortAscending} from "react-icons/ai";
import MobileNavigationHeader from './MobileNavigationHeader'
import CarItemComponent from './CarItemComponent';
import {MdKeyboardArrowDown, MdKeyboardArrowUp} from 'react-icons/md'
import show_password_icon from './assets/password-show.svg';
import RangeInputComponent from './RangeInputComponent';
import PaginationComponent from './common/PaginationComponent';
import { Link, useLocation, useNavigate, useSearchParams} from 'react-router-dom';
import FooterComponent from './common/FooterComponent';
import Select from 'react-select';
import { customStylesSort } from './common/CustomSelectStyles';
import axios from "axios"
import PostService from './services/PostService';
import SkeletonCarCard from './skeleton/SkeletonCarCard';
import {BiChevronDown} from 'react-icons/bi'
import SearchBarComponent from './common/SearchBarComponent';
import car from "./assets/car-post.svg"
import no_car_preview from "./assets/no-car-preview.jpg"
import post_not_found_illustration from "./assets/post-not-found-illustration.svg"
import Brands from "./utils/brand_model.json"
import provincesAndCities from "./utils/province_city.json"
import WatchlistPostService from './services/WatchlistPostService';
import { toast, ToastContainer } from 'react-toastify';

const CarListPage = (props) => {

  const options = [
    { value: '1', label: 'Price low - high' },
    { value: '2', label: 'Price high - low' },
    { value: '3', label: 'Date newest - oldest' },
    { value: '4', label: 'Date oldest - latest' }
];


  const navigate = useNavigate();

    const MIN_VALUE = 0
    const MAX_VALUE = 5000000
    const [value, setValue] = useState([MIN_VALUE, MAX_VALUE]);

    const handleChange = (event, newValue) => {
      setValue(newValue);


    };

    let list1 = provincesAndCities.map(province => province.cities)[0].map(city => city.city);
    let list2 = provincesAndCities.map(province => province.cities)[1].map(city => city.city);
    let list3 = provincesAndCities.map(province => province.cities)[2].map(city => city.city);
    let list4 = provincesAndCities.map(province => province.cities)[3].map(city => city.city);
    let list5 = provincesAndCities.map(province => province.cities)[4].map(city => city.city);
    let list6 = provincesAndCities.map(province => province.cities)[5].map(city => city.city);
    let list7 = provincesAndCities.map(province => province.cities)[6].map(city => city.city);
    let list8 = provincesAndCities.map(province => province.cities)[7].map(city => city.city);
    let list9 = provincesAndCities.map(province => province.cities)[8].map(city => city.city);
    let allCitiesList = [...list1, ...list2, ...list3, ...list4, ...list5, ...list6, ...list7, ...list8, ...list9]

    const [selectedLocation, setSelectedLocation] = useState({
      province: null,
      city: []
    });

    const [loading ,setLoading] = useState();
    const [resultCount, setResultCount] = useState(0);
    const [posts, setPosts] = useState([]);
    //const [activePosts, setActivePosts] = useState([]);
    const [brandCount,setBrandCount] = useState({})
    const [provinceCount,setProvinceCount] = useState({})
    const [totalPostCount, setTotalPostCount] = useState(0);
    const [pageCount, setPageCount] = useState(0);
    const [postsPageSize, setPostsPageSize] = useState(5); // total elements to display per page (DEFAULT)
    const [postsPageNo, setPostsPageNo] = useState(1); // start at the first page (index of 0 from backend)
    const [sortBy, setSortBy] = useState();
    const [sortDir, setSortDir] = useState();

    
    const sortAsc = "asc";
    const sortDesc = "desc";
    let sort = "";
    let filterByBrands = "";

    const [searchParams] = useSearchParams();
    const [selectedBrands, setSelectedBrands] = useState([])
    const [searchQuery, setSearchQuery] = useState(searchParams.get("query"));

    const brands =Brands.map(p => p.brand).sort();



    const [error, setError] = useState({
      postNotFoundError: ""
    });

    const setFetchSuccessState = (posts, totalItems, totalPages) => {

      setLoading(false)
      setResultCount(totalItems)
      setPosts(posts)

      setPageCount(totalPages);
    }

    const setFetchErrorState = () => {
      setPageCount(1);
      setPosts([])
      setResultCount(0)
      setLoading(false)
    }
  


  const searchPosts = () => {
    const params = getRequestParams(null, searchQuery, selectedLocation.province, selectedLocation.city, postsPageNo, postsPageSize, sortBy, sortDir, null, null);

    setLoading(true)
    PostService.getAllPosts(params)
      .then(res => {
        const { totalItems, totalPages, currentPage, posts } = res.data;
        setFetchSuccessState(posts, totalItems, totalPages)

      }).catch(err => {
        //toast.error("There was an error performing this action. Please try again shortly")
        setError({
          postNotFoundError: err.response.data.postNotFound
        })
        setFetchErrorState()
      })

  }

  const searchByLocationTag = (city) => {

    const selectedCity = city;
    setSelectedLocation(prev => ({
      ...prev,
      city: selectedCity,
      province: null
    }))

    const params = getRequestParams(null, searchQuery, null, selectedCity, postsPageNo, postsPageSize, sortBy, sortDir, null, null);
    getAllPosts(params);

  }

  const addWatchlistPost = (id) => {
    WatchlistPostService.addWatchlistPost(id)
      .then(res => {
        toast.success(res.data)
      }).catch(err => {
        if(err.response.data.postNotFound) {
          toast.error(err.response.data.postNotFound)
        } else {
          toast.error("There was an error watchlisting this post, please try again later")
        }
      });
  }

  const sortPostsByPrice = (sortDir) => {
      sort = "price";
      setSortBy(sort);
      const params = getRequestParams(null, null, null, null, postsPageNo, postsPageSize, sort, sortDir, null, null);
      getAllPosts(params)
  }

  const sortPostsByDate = (sortDir) => {
    sort = "createdAt";
    setSortBy(sort);
    const params = getRequestParams(null, null, null, null, postsPageNo, postsPageSize, sort, sortDir, null, null);
    getAllPosts(params)
  }
    
    const resetAllFilters = () => {
      setSearchQuery("")
      // reset price ranges 
      setValue([MIN_VALUE, MAX_VALUE]);
      // load all posts
      getAllPosts(getRequestParams(null, null, null, null, postsPageNo, postsPageSize, sortBy, sortDir, null, null));
      // reset selected brands to empty arr
      setSelectedBrands([])
      // reset selected location to empty arr
      setSelectedLocation([])


    }


    const filterPosts = () => {
      filterByBrands = selectedBrands.join(",")

      const params = getRequestParams(filterByBrands, null, selectedLocation.province, selectedLocation.city, postsPageNo, postsPageSize, sortBy, sortDir, value[0], value[1]);
      setLoading(true);

      PostService.getAllPosts(params)
        .then(res => {
          const { totalItems, totalPages, currentPage, posts } = res.data;
          setFetchSuccessState(posts, totalItems, totalPages)
          // setTotalPostCount(totalItems)
        }).catch(err => {
          setFetchErrorState()
        })
      
    
    }

    const onQueryChange = (event) => {
      setSearchQuery(event.target.value);
     
    };

    
    const onSortChange = (event) => {
  
    if (event.value === options[0].value) {
        setSortDir(sortAsc);
        sortPostsByPrice(sortAsc);
    } else if (event.value === options[1].value) {
        setSortDir(sortDesc);
        sortPostsByPrice(sortDesc);

    } else if (event.value === options[2].value) {
        sortPostsByDate("asc");
        setSortDir("asc");
    } else {
        sortPostsByDate("desc");
        setSortDir("desc");
    }

   


    }

    const onPageChange = (event, value) => {
      setPostsPageNo(value);
    };
  

    const getRequestParams = (brand, query, province, city, postsPageNo, postsPageSize, sortBy, sortDir, minValue, maxValue) => {
      let params = {};
  
      // these params must match on the backend (projectName, postsPageNo, size)
      if (query) params["query"] = query;
      if(brand) params["brand"] = brand;
      if(province) params["province"] = province;
      if(city) params["city"] = city;
      if (postsPageNo) params["pageNo"] = postsPageNo - 1;
      if (postsPageSize) params["pageSize"] = postsPageSize;
      if(sortBy) params["sortBy"] = sortBy;
      if(sortDir) params["sortDir"] = sortDir;
      if(minValue) params["minValue"] = minValue;
      if(maxValue) params["maxValue"] = maxValue;
  
      return params;
    };




 
    const getAllPosts = (requestParams) => {
      // const list = [];
      setLoading(true);
        
      PostService.getAllPosts(requestParams)
      .then(res => {
          const {totalItems, totalPages, currentPage, posts} = res.data;
          setLoading(false);
          setFetchSuccessState(posts, totalItems, totalPages)
      }).catch(err => {
          setError({ postNotFoundError: err.response})
          setFetchErrorState()
         
      })
    }


    const getPostCountByBrandAndProvince = () => {
      let total = 0;
      PostService.getPostCountByBrand()
        .then(res => {
          setBrandCount(res.data)
          for(let c in res.data) {
            total += res.data[c]
          }
          setTotalPostCount(total)
        }).catch(() => {
          toast.error("There was an error performing this action. Please try again shortly")

        })

        PostService.getPostCountByProvince()
        .then(res => {
          setProvinceCount(res.data)
        }).catch(() => {
          toast.error("There was an error performing this action. Please try again shortly")

        })
    }

   // {provinceList[index].cities.map(city => city.city
    useEffect(() => {
      getAllPosts(getRequestParams(selectedBrands, searchQuery, selectedLocation.province, selectedLocation.city, postsPageNo, postsPageSize, sortBy, sortDir, value[0], value[1]));
      //getPostCountByBrandAndProvince()
    }, [postsPageNo, postsPageSize]);


  
    
    const handleBrandChange = (event) => {

      const { id, name, checked } = event.target;
      // checked
      setSelectedBrands([...selectedBrands, name]);

      // unchecked
      if (!checked) {
        setSelectedBrands(selectedBrands.filter((item) => item !== name));
      }

    }
 
    const handleLocationChange = (event) => {
      const {id, name, value} = event.target;
      const parsedId = JSON.parse(id);
      const provinces = provincesAndCities.map(province => province.province)

      let selectedProvince;
      let selectedCity;
      
  
      // province
      if(provinces.includes(allCitiesList[parsedId])) {

        selectedProvince = allCitiesList[parsedId];
        setSelectedLocation(prev => ({
          ...prev,
          city: null,
          province: selectedProvince
        }))
    
      } // city
      else {
      
        selectedCity = allCitiesList[parsedId];
        setSelectedLocation(prev => ({
          ...prev, 
          city: selectedCity,
          province: null
  
        }))                               
      }


      const params = getRequestParams(null, null, selectedProvince, selectedCity, postsPageNo, postsPageSize, sortBy, sortDir, null,null);
  
      getAllPosts(params)

      
    }

    const brandCheckboxes = brands.map((brand, id) => {

      return (
        <div>
         <p className="brand-option">
           <input key={id}  type="checkbox" id={id} onChange={handleBrandChange} name={brand} checked={selectedBrands.includes(brand)} style={{marginRight: ".5rem"}}/>

           {brand} <span>({Object.values(brandCount).map(count => count)[id]})</span>                    
     
         </p>
        </div>
      )

    });
    


    return (
      <>

    <MobileNavigationHeader heading="Showing results for" infoDefault={searchQuery ? searchQuery : "All Posts"} infoDefault2={" (" + resultCount + ")"} />


      <div className="car-list-page">
        <ToastContainer style={{marginTop: "3rem"}}/>
        <div className="desktop-accordion-container">

      
          <div className="filter-content">
            <h3>Refine your search</h3>
            <p className='text-muted'>Try adjusting your search to find your desired results.</p>
            <button className="canvas-reset-filter-btn" onClick={resetAllFilters}>Reset filters</button>
          <div className="desktop-accordion">
              <div class="accordion desktop-accordion accordion-flush" id="accordionPanelsStayOpenExample">
                <div class="accordion-item">
                  <h2 class="desktop-accordion-header" id="panelsStayOpen-headingThree">
                    <button
                      class="accordion-button shadow-none"
                      type="button"
                      id="accordion-default-icon-dark"
                      data-bs-toggle="collapse"
                      data-bs-target="#panelsStayOpen-collapseThree"
                      aria-expanded="true"
                      aria-controls="panelsStayOpen-collapseThree"
                    >
                      Price <BiChevronDown id="accordion-default-icon-dark" size={30}/>
                    </button>
                  </h2>

                  <div id="panelsStayOpen-collapseThree" class="accordion-collapse collapse" aria-labelledby="panelsStayOpen-headingThree">

                    <div class="accordion-body accordion-body-main">
                      
                      <div className="form-group row price-form">
                        <RangeInputComponent handleChange={handleChange} value={value} minValue={MIN_VALUE} maxValue={MAX_VALUE} />
                      </div>
                    </div>

                  </div>

                </div>
              </div>


            
              <div className="desktop-accordion">
                <div class="accordion accordion-flush" id="accordionPanelsStayOpenExample">
                      <div class="accordion-item">
                        <h2 class="desktop-accordion-header" id="panelsStayOpen-headingThree">
                          <button
                            class="accordion-button shadow-none"
                            id="accordion-default-icon-dark"
                            type="button"
                            data-bs-toggle="collapse"
                            data-bs-target="#panelsStayOpen-collapseOne"
                            aria-expanded="true"
                            aria-controls="panelsStayOpen-collapseOne"
                          >
                            Brands <BiChevronDown id="accordion-default-icon-dark" size={30}/>
                          </button>
                        </h2>

                        <div id="panelsStayOpen-collapseOne" class="accordion-collapse collapse" aria-labelledby="panelsStayOpen-headingOne">

                          <div class="accordion-body">
                            {brandCheckboxes}
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                <div className="desktop-accordion">

                  <div class="accordion accordion-flush" id="accordionPanelsStayOpenExample">
                    <div class="accordion-item">
                      <h2 class="desktop-accordion-header" id="panelsStayOpen-headingThree">
                        <button class="accordion-button shadow-none" type="button" id="accordion-default-icon-dark" data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-collapseTwo" aria-expanded="true" aria-controls="panelsStayOpen-collapseTwo">
                          Location <BiChevronDown id="accordion-default-icon-dark" size={30} />
                        </button>
                      </h2>

                      <div id="panelsStayOpen-collapseTwo" class="accordion-collapse collapse" aria-labelledby="panelsStayOpen-headingTwo">
                        <div class="accordion-body location-body accordion-body-main">
                          <h2 className="location-sub-heading">South Africa <span>({totalPostCount})</span> </h2>

                          <div class="accordion accordion-flush" id="accordionPanelsStayOpenExample" >

                            {provincesAndCities.map((province, index) => {
                              return (
                                <div class="accordion-item">
                                  <h2 class="accordion-header" id={`panelsStayOpen-headingProvinces-${index}`}>
                                    <button class="accordion-button provinces-accordion-button shadow-none" id="accordion-default-icon" type="button" data-bs-toggle="collapse" data-bs-target={`#panelsStayOpen-collapseProvinces-${index}`} aria-expanded="true" aria-controls="panelsStayOpen-collapseProvinces"  >
                                      <p className="province-option">{province.province} <BiChevronDown id="accordion-default-icon-dark" size={25} /> <span> ({Object.values(provinceCount).map(count => count)[index]})</span></p>
                                    </button>
                                  </h2>

                                  <div id={`panelsStayOpen-collapseProvinces-${index}`}  class="accordion-collapse collapse" aria-labelledby={`panelsStayOpen-headingProvinces-${index}`}>

                                    <div class="accordion-body province-body">
                                      <ul>

                                        {provincesAndCities[index].cities.map(p => {return <li key={p.id} id={p.id} onClick={handleLocationChange} className="province-city">{p.city}</li>})}
                                      </ul>

                                    </div>
                                  </div>
                                </div>

                              )
                            })}

                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>



                <button className="filter-search-btn mt-1" onClick={() => filterPosts()}>Search</button>



              </div>  
          </div>
        </div>

        <div className="content">
          

   
          <div className="sort-filter-container">
            <div className="filter-container mt-s4">
              <button className="shadow-none filter-btn" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasExample2" aria-controls="offcanvasExample2">
                <VscFilter size={15} className="filter-icon" /> Filter {resultCount} results
              </button>

              <div class="offcanvas  offcanvas-start" tabindex="-1" id="offcanvasExample2" aria-labelledby="offcanvasExampleLabel">
                <div class="offcanvas-header mt-4">
                  <div className="canvas-header">
                    <h2 className="canvas-heading">Refine your search</h2>
                      <p className="canvas-info text-muted">Try adjusting your search to find your desired results.</p>
                    <button className="canvas-reset-filter-btn" data-bs-dismiss="offcanvas" onClick={resetAllFilters}>Remove filters</button>
                  </div>
                  <button type="button" class="btn-close text-reset shadow-none" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                </div>

                <div class="offcanvas-body filter-content">

                  <div className="price-filter mt-5">
                    <div class="accordion accordion-flush" id="accordionPanelsStayOpenExample">
                      <div class="accordion-item">
                        <h2 class="accordion-header" id="panelsStayOpen-headingThree">
                          <button class="accordion-button shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-collapseThree" aria-expanded="true" aria-controls="panelsStayOpen-collapseThree">
                            Price <BiChevronDown id="accordion-default-icon-dark" size={30}/>
                          </button>
                        </h2>

                        <div id="panelsStayOpen-collapseThree" class="accordion-collapse collapse" aria-labelledby="panelsStayOpen-headingThree">

                          <div class="accordion-body accordion-body-main">
                            
                            <div className="form-group row price-form">
                              <RangeInputComponent handleChange={handleChange} value={value} minValue={MIN_VALUE} maxValue={MAX_VALUE} />
                            </div>
                          </div>

                        </div>

                      </div>
                    </div>
                  </div>



                  <div className="brand-filter mt-5">
                    <div class="accordion accordion-flush" id="accordionPanelsStayOpenExample" >
                      <div class="accordion-item">
                        <h2 class="accordion-header" id="panelsStayOpen-headingOne">

                 
                          <button class="accordion-button shadow-none" type="button"  data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-collapseOne" aria-expanded="true" aria-controls="panelsStayOpen-collapseOne">
                            Brands <BiChevronDown id="accordion-default-icon-dark" size={30}/>
                          </button>
                        </h2>

                        <div id="panelsStayOpen-collapseOne" class="accordion-collapse collapse" aria-labelledby="panelsStayOpen-headingOne">

                          <div class="accordion-body brand-body accordion-body-main">
                            {brandCheckboxes} 
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div className="location-filter mt-5">

                    <div class="accordion accordion-flush" id="accordionPanelsStayOpenExample" >
                      <div class="accordion-item">
                        <h2 class="accordion-header" id="panelsStayOpen-headingTwo">
                          <button class="accordion-button shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-collapseTwo" aria-expanded="true" aria-controls="panelsStayOpen-collapseTwo">
                            Location <BiChevronDown id="accordion-default-icon-dark" size={30}/>
                          </button>
                        </h2>

                        <div id="panelsStayOpen-collapseTwo" class="accordion-collapse collapse" aria-labelledby="panelsStayOpen-headingTwo">
                          <div class="accordion-body location-body accordion-body-main">
                            <h2 className="location-sub-heading">
                              South Africa <span>({totalPostCount})</span>
                            </h2>

                                    <div class="accordion accordion-flush" id="accordionPanelsStayOpenExample" >

				{provincesAndCities.map((province, index) => {
                                  return (
                                    <div class="accordion-item">
                                      <h2 class="accordion-header" id={`panelsStayOpen-headingProvinces-${index}`}>
                                        <button class="accordion-button provinces-accordion-button shadow-none" id="accordion-default-icon" type="button" data-bs-toggle="collapse" data-bs-target={`#panelsStayOpen-collapseProvinces-${index}`} aria-expanded="true" aria-controls="panelsStayOpen-collapseProvinces"  >
                                          <p className="province-option">{province.province} <BiChevronDown id="accordion-default-icon-dark" size={25} /> <span> ({Object.values(provinceCount).map(count => count)[index]})</span></p>
                                        </button>
                                      </h2>

                                      <div id={`panelsStayOpen-collapseProvinces-${index}`} class="accordion-collapse collapse" aria-labelledby={`panelsStayOpen-headingProvinces-${index}`}>

                                        <div class="accordion-body province-body">
                                          <ul>

                                            {provincesAndCities[index].cities.map(p => { return <li key={p.id} id={p.id} onClick={handleLocationChange} className="province-city">{p.city}</li> })}
                                          </ul>

                                        </div>
                                      </div>
                                    </div>

                                  )
                                })}
                                    </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>



                  <button className="filter-search-btn mt-5" data-bs-dismiss="offcanvas"  onClick={() => filterPosts()}>Search</button>
                </div>
              </div>

              {/* End filter*/}
            </div>

            <div class="dropdown sort-container mt-3" style={{display: "flex", justifyContent: "flex-end"}}>
                
                <Select
               
                    onChange={onSortChange}
                    placeholder={`Sort ${resultCount} results`}
                    options={options}
          
                    id="sort-select"
                    isSearchable={false}
                    styles={customStylesSort}
                    
                  />

                
            </div>
            <SearchBarComponent 
              name={"query"} 
              value={searchQuery}   
              
               
              formClass={"landing-search-form"} 
              onChange={onQueryChange}
              searchIconSize={22} 
              onClick={searchPosts}
            />

        

            {/* End sort-filter-container*/}
          </div>

          <div className="car-item-container mt-5">
            
              {loading ?
                <SkeletonCarCard /> :

                // if post exists
                posts && (
                  // if posts length > 0
                  posts.length > 0 ? 
                    posts.map((post, id) => {
                      return (
                        <>

                            {post.postImages[0] ?
                              <CarItemComponent viewPostOnClick={() => navigate(`/car-info/${post.id}`)} imageUrl={post.postImages[0].imageUrl} 
                                key={post.id} time={post.createdAt} addWatchlistPostOnClick={() => addWatchlistPost(post.id)} title={post.title} 
                                searchByLocationTag={() => searchByLocationTag(post.city)}
                                price={post.price} description={post.description} location={post.city} />
                              :
                              <CarItemComponent viewPostOnClick={() => navigate(`/car-info/${post.id}`)} noPreviewImage={no_car_preview} 
                                key={post.id}  time={post.createdAt} addWatchlistPostOnClick={() => addWatchlistPost(post.id)} title={post.title} 
                                searchByLocationTag={() =>searchByLocationTag(post.city)}
                                price={post.price} description={post.description} location={post.city} />
                            }
                          {/* </Link> */}
                        </>
                      )
                      
                    })
                    :
                    <div className="post-not-found-container">
                      <h2>No posts found</h2>
                      <p>Please try again</p>
                      <img src={post_not_found_illustration} />
                    </div>
                )
                
              }

  
          </div>
            <PaginationComponent
              className="my-3"
              count={pageCount}
              page={postsPageNo}
              siblingCount={1}
              boundaryCount={1}
              variant="outlined"
              shape="rounded"
              onPageChange={onPageChange}
            />

          {/* End content*/}
        </div>
        {/* End car-list-page*/}

  
      </div>
        

    
      <FooterComponent />
      </>
    );
}

export default CarListPage
