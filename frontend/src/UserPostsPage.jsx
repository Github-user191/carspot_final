import React, { useState, useEffect } from "react";
import MobileNavigationHeader from "./MobileNavigationHeader";
import { AiOutlineSortAscending } from "react-icons/ai";
import CarItemComponent from "./CarItemComponent";
import PaginationComponent from "./common/PaginationComponent";
import FooterComponent from "./common/FooterComponent";
import PostService from "./services/PostService";
import { Link, useParams } from "react-router-dom";
import Select from "react-select";
import { customStylesSort } from "./common/CustomSelectStyles";
import UserService from "./services/UserService";
import no_car_preview from "./assets/no-car-preview.jpg";
import SkeletonCarCard from "./skeleton/SkeletonCarCard";
import post_not_found_illustration from "./assets/post-not-found-illustration.svg";
import WatchlistPostService from "./services/WatchlistPostService";

const UserPostsPage = () => {
    const [loading, setLoading] = useState();

    const [activePosts, setActivePosts] = useState({});
    const [activePostCount, setActivePostCount] = useState(0);
    const [activePostsPageCount, setActivePostsPageCount] = useState(0);
    const [activePostsPageNo, setActivePostsPageNo] = useState(1); // start at the first page (index of 0 from backend)
    const [activePostsPageSize, setActivePostsPageSize] = useState(5); // total elements to display per page (DEFAULT)
    const [deleteActivePostCount, setDeleteActivePostCount] = useState(0);

    const [allPosts, setAllPosts] = useState({});
    const [allPostCount, setAllPostCount] = useState(0);
    const [allPostsPageCount, setAllPostsPageCount] = useState(0);
    const [allPostsPageNo, setAllPostsPageNo] = useState(1); // start at the first page (index of 0 from backend)
    const [allPostsPageSize, setAllPostsPageSize] = useState(5); // total elements to display per page (DEFAULT)
    const [deleteAllPostCount, setDeleteAllPostCount] = useState(0);

    const [watchlistPosts, setWatchlistPosts] = useState({});
    const [watchlistedPostCount, setWatchlistedPostCount] = useState(0);
    const [watchlistPostsPageCount, setWatchlistPostsPageCount] = useState(0);
    const [watchlistPostsPageNo, setWatchlistPostsPageNo] = useState(1); // start at the first page (index of 0 from backend)
    const [watchlistPostsPageSize, setWatchlistPostsPageSize] = useState(5); // total elements to display per page (DEFAULT)
    const [deleteWatchlistPostCount, setDeleteWatchlistPostCount] = useState(0);


    const [sortBy, setSortBy] = useState();
    const [sortDir, setSortDir] = useState();


    const sortAsc = "asc";
    const sortDesc = "desc";
    let sort = "";

    const [idList, setIdList] = useState([]);

    const deleteWatchlistPostsHandler = (event) => {
        setLoading(true)
        if (idList.length > 0) {
            WatchlistPostService.deleteWatchlistPosts(idList)
                .then(res => {
                    const { totalItems, totalPages, currentPage, posts } = res.data;

                    setLoading(false);
                    setWatchlistPosts(posts);
                    setWatchlistedPostCount(totalItems);
                    setWatchlistPostsPageCount(totalPages)
                    setDeleteWatchlistPostCount(0)
                }).catch(err => {
                    if (err) {
                        setWatchlistPosts([])
                        setDeleteWatchlistPostCount(0)
                        setWatchlistedPostCount(0)
                        setLoading(false);
                    }
                }).finally(() => {
                    setIdList([])
                })
        }

    };


    const deleteActivePostsHandler = (event) => {
        setLoading(true)
        console.log(idList)

        PostService.deletePosts(idList)
        .then((res) => {
            const { totalItems, totalPages, currentPage, posts } = res.data;
            setActivePosts(posts);
            setActivePostCount(totalItems)
            setActivePostsPageCount(totalPages);
            setDeleteActivePostCount(0)
            setLoading(false);
        })
        .catch((err) => {
            if (err) {
                setActivePosts([])
                setDeleteActivePostCount(0)
                setActivePostCount(0)
                setLoading(false);
            }
        }).finally(() => {
            setIdList([])
        })
    };

    const handleActivePostChange = (event) => {
        let tempList = [...idList];
        const { id, value, checked } = event.target;

        if (checked) {
            tempList.push(event.target.id);
            setDeleteActivePostCount(prev => prev + 1);
        } else {
            tempList.pop(event.target.id);
            setDeleteActivePostCount(prev => prev - 1);
        }
        console.log(tempList)
        setIdList(tempList);
    };

    const handleWatchlistPostChange = (event) => {
        let tempList = [...idList];

        const { value, checked } = event.target;
        if (checked) {
            tempList.push(event.target.id);
            setDeleteWatchlistPostCount(prev => prev + 1);
        } else {
            tempList.pop(event.target.id);
            setDeleteWatchlistPostCount(prev => prev - 1);
        }
        setIdList(tempList);
    };

    const options = [
        { value: "1", label: "Price low - high" },
        { value: "2", label: "Price high - low" },
        { value: "3", label: "Date newest - oldest" },
        { value: "4", label: "Date oldest - latest" },
    ];

    const onActivePostsPageChange = (event, value) => {
        setActivePostsPageNo(value);
    };

    const onAllPostsPageChange = (event, value) => {
        setAllPostsPageNo(value);
    };

    const onWatchlistPostsPageChange = (event, value) => {
        setWatchlistPostsPageNo(value);
    };

    const getRequestParams = (brand, query, province, city, pageNo, pageSize, sortBy, sortDir, minValue, maxValue) => {
        let params = {};

        // these params must match on the backend (projectName, pageNo, size)
        if (query) params["query"] = query;
        if (brand) params["brand"] = brand;
        if (province) params["province"] = province;
        if (city) params["city"] = city;
        if (pageNo) params["pageNo"] = pageNo - 1;
        if (pageSize) params["pageSize"] = pageSize;
        if (sortBy) params["sortBy"] = sortBy;
        if (sortDir) params["sortDir"] = sortDir;
        if (minValue) params["minValue"] = minValue;
        if (maxValue) params["maxValue"] = maxValue;
        return params;
    };

    const sortPostsByPrice = (sortDir) => {
        sort = "price";
        setSortBy(sort)
        const params = getRequestParams(null, null, null, null, allPostsPageNo, activePostsPageSize, sort, sortDir, null, null);
        getUserActivePosts(params)
    };

    const sortPostsByDate = (sortDir) => {
        sort = "createdAt";
        setSortBy(sort);
        const params = getRequestParams(null, null, null, null, allPostsPageNo, activePostsPageSize, sort, sortDir, null, null);
        getUserActivePosts(params)
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
    };

    const getUserWatchlistPosts = (requestParams) => {
        setLoading(true);

        console.log(requestParams)
        WatchlistPostService.getUserWatchlistPosts(requestParams)
            .then((res) => {
                const { totalItems, totalPages, currentPage, posts } = res.data;
              
                setLoading(false);
                setWatchlistPosts(posts);
                setWatchlistedPostCount(totalItems);
                setWatchlistPostsPageCount(totalPages);
            })
            .catch((err) => {
                
                setWatchlistPostsPageCount(1);
                setWatchlistPosts([]);
                setWatchlistedPostCount(0);
            }).finally(() => setLoading(false))
    };

    const getUserActivePosts = (requestParams) => {
        setLoading(true);

        PostService.getUserActivePosts(requestParams)
            .then((res) => {
                const { totalItems, totalPages, currentPage, posts } = res.data;

                setLoading(false);
                setActivePosts(posts);
                setActivePostCount(totalItems);
                setActivePostsPageCount(totalPages)
            })
            .catch((err) => {
                setActivePostsPageCount(1);
                setActivePosts([]);
                setActivePostCount(0);
            })
            .finally(() => setLoading(false))
    };

    const getAllUserPosts = (requestParams) => {
        setLoading(true);

        PostService.getAllUserPosts(requestParams)
            .then((res) => {
                const { totalItems, totalPages, currentPage, posts } = res.data;
                setLoading(false);
                setAllPosts(posts);
                setAllPostCount(totalItems);
                setAllPostsPageCount(totalPages)
            })
            .catch((err) => {
                setAllPostsPageCount(1);
                setAllPosts([]);
                setAllPostCount(0);
            })
            .finally(() => setLoading(false))
    };


    useEffect(() => {
        getUserActivePosts(getRequestParams(null, null, null, null, activePostsPageNo, activePostsPageSize, sortBy, sortDir, null, null)); 
        getAllUserPosts(getRequestParams(null, null, null, null, allPostsPageNo, allPostsPageSize, sortBy, sortDir, null, null));
        getUserWatchlistPosts(getRequestParams(null, null, null, null, watchlistPostsPageNo, watchlistPostsPageSize, null, null, null, null))
    }, [activePostsPageNo, activePostsPageSize,activePostCount, allPostsPageNo, allPostsPageSize, watchlistPostsPageNo, watchlistPostsPageSize]);

    return (
        <div className="user-ads-page">

            <MobileNavigationHeader heading={"Your Posts"} />
            <div className="content">
                <ul class="nav nav-pills mb-3 user-ads-tab" id="pills-tab" role="tablist">
                    <li class="nav-item" role="presentation">
                        <button class="nav-link active tab-btn" id="pills-home-tab" data-bs-toggle="pill" data-bs-target="#pills-home" type="button" role="tab" aria-controls="pills-home" aria-selected="true">
                            Active ({activePostCount})
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link tab-btn" id="pills-profile-tab" data-bs-toggle="pill" data-bs-target="#pills-profile" type="button" role="tab" aria-controls="pills-profile" aria-selected="false">
                            All ({allPostCount})
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link tab-btn" id="pills-contact-tab" data-bs-toggle="pill" data-bs-target="#pills-contact" type="button" role="tab" aria-controls="pills-contact" aria-selected="false" >
                            Watchlisted ({watchlistedPostCount})
                        </button>
                    </li>
                </ul>

                <div className="user-ad-info">
                    <div class="tab-content" id="pills-tabContent">

                        {/* ACTIVE POSTS TAB */}
                        <div class="tab-pane fade show active active-ads-tab" id="pills-home" role="tabpanel" aria-labelledby="pills-home-tab"  >
    
                            <div className="btn-container">

        
                                <button className={deleteActivePostCount > 0 ? "delete-btn" : "delete-btn delete-btn-disabled"} onClick={deleteActivePostsHandler} disabled={deleteActivePostCount > 0 ? false : true}>

                                    Delete {deleteActivePostCount < 1 ? "" : deleteActivePostCount} Post(s)
                                </button>
                                <div className="sort-your-ads-container mt-2">
                                    <Select onChange={onSortChange} placeholder={`Sort ${activePostCount} results`}
                                        options={options} className="mb-1" id="sort-select-user-ads" isSearchable={false} styles={customStylesSort} />
                                </div>
                            </div>

                            <div className="car-card-container">
                                {loading ? (
                                    <SkeletonCarCard />
                                ) : (
                                    activePosts &&
                                    (activePosts.length > 0 ? (
                                        activePosts.map((post) => {
                                            return (
                                                <>
                                                    <div className="delete-ad-input-container">
                                                        <input type="checkbox" name="title" value={post.title} id={post.id}
                                                            className="shadow-none form-check-input" onChange={handleActivePostChange} />
                                                        <label>Select</label>
                                                    </div>

                                                    <Link to={`/car-info/${post.id}`} style={{ textDecoration: "none" }}>
                                                        <div className="car-card-container">
                                                            {post.postImages[0] ? (
                                                                <CarItemComponent imageUrl={post.postImages[0].imageUrl} key={post.id} time={post.createdAt} title={post.title} price={post.price} description={post.description} location={post.city} />
                                                            ) : (
                                                                <CarItemComponent noPreviewImage={no_car_preview} key={post.id} time={post.createdAt} title={post.title} price={post.price} description={post.description} location={post.city} />
                                                            )}
                                                        </div>
                                                    </Link>
                                                </>
                                            );
                                        })
                                    ) : (
                                        <div className="post-not-found-container">
                                            <h2>No Posts found</h2>
                                            <p>Please try again</p>
                                            <img src={post_not_found_illustration} />
                                        </div>
                                    ))
                                )}
                                <PaginationComponent className="my-3" count={activePostsPageCount} page={activePostsPageNo} siblingCount={1}
                                    boundaryCount={1} variant="outlined" shape="rounded" onPageChange={onActivePostsPageChange}
                                />

                            </div>
                        </div>


                        {/* ALL POSTS TAB */}
                        <div class="tab-pane fade all-ads-tab" id="pills-profile" role="tabpanel" aria-labelledby="pills-profile-tab">
        
                            <div className="btn-container">

                            </div>

                            <div className="car-card-container">
                                {loading ? (
                                    <SkeletonCarCard />
                                ) : (
                                    allPosts &&
                                    (allPosts.length > 0 ? (
                                        allPosts.map((post) => {
                                            return (
                                                <>
                        

                                                    <Link to={`/car-info/${post.id}`} style={{ textDecoration: "none" }}>
                                                        <div className="car-card-container">
                                                            {post.postImages[0] ? (
                                                                <CarItemComponent imageUrl={post.postImages[0].imageUrl} key={post.id} time={post.createdAt} title={post.title} price={post.price} description={post.description} location={post.city} />
                                                            ) : (
                                                                <CarItemComponent noPreviewImage={no_car_preview} key={post.id} time={post.createdAt} title={post.title} price={post.price} description={post.description} location={post.city} />
                                                            )}
                                                        </div>
                                                    </Link>
                                                </>
                                            );
                                        })
                                    ) : (
                                        <div className="post-not-found-container">
                                            <h2>No Posts found</h2>
                                            <p>Please try again</p>
                                            <img src={post_not_found_illustration} />
                                        </div>
                                    ))
                                )}
    
                                <PaginationComponent className="my-3" count={allPostsPageCount} page={allPostsPageNo} siblingCount={1}
                                    boundaryCount={1} variant="outlined" shape="rounded" onPageChange={onAllPostsPageChange} />
                            </div>


                        </div>

                        {/* WATCHLISTED POSTS TAB */}
                        <div class="tab-pane fade watchlisted-ads-tab" id="pills-contact" role="tabpanel" aria-labelledby="pills-contact-tab">
        
                            <div className="btn-container">

    
                                <button className={deleteWatchlistPostCount > 0 ? "delete-btn" : "delete-btn delete-btn-disabled"} onClick={deleteWatchlistPostsHandler}
                                    disabled={deleteWatchlistPostCount > 0 ? false : true}>
                                    Delete {deleteWatchlistPostCount < 1 ? "" : deleteWatchlistPostCount} ad(s)
                                </button>
                            </div>

                            <div className="car-card-container">
                                {loading ? (
                                    <SkeletonCarCard />
                                ) : (
                                    watchlistPosts &&
                                    (watchlistPosts.length > 0 ? (
                                        watchlistPosts.map((post) => {
                                            return (
                                                <>
                                                    <div className="delete-ad-input-container">
                                                        <input type="checkbox" name="title" value={post.title} id={post.id} className="shadow-none form-check-input" onChange={handleWatchlistPostChange} />
                                                        <label>Select</label>
                                                    </div>

                                                    <Link to={`/car-info/${post.id}`} style={{ textDecoration: "none" }}>
                                                        <div className="car-card-container">
                                                            {post.postImages[0] ? (
                                                                <CarItemComponent imageUrl={post.postImages[0].imageUrl} key={post.id} time={post.createdAt} title={post.title} price={post.price} description={post.description} location={post.city} />
                                                            ) : (
                                                                <CarItemComponent noPreviewImage={no_car_preview} key={post.id} time={post.createdAt} title={post.title} price={post.price} description={post.description} location={post.city} />
                                                            )}
                                                        </div>
                                                    </Link>
                                                </>
                                            );
                                        })
                                    ) : (
                                        <div className="post-not-found-container">
                                            <h2>No Watchlisted Posts found</h2>
                                            <p>Please try again</p>
                                            <img src={post_not_found_illustration} />
                                        </div>
                                    ))
                                )}

                                <PaginationComponent className="my-3" count={watchlistPostsPageCount} page={watchlistPostsPageNo} siblingCount={1}
                                    boundaryCount={1} variant="outlined" shape="rounded" onPageChange={onWatchlistPostsPageChange} />
                            </div>


                        </div>


                    </div> {/* end tab-content*/}
                </div> {/* end user-ad-info*/}
            </div >


            <div id="footer-component">
                <FooterComponent />
            </div>
        </div >
    );
};

export default UserPostsPage;
