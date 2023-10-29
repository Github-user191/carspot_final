import React, { useState,useEffect } from 'react'
import MobileNavigationHeader from './MobileNavigationHeader'
import defaultAvatar from "./assets/user-profile.svg";
import FooterComponent from './common/FooterComponent';
import { BiPlus } from 'react-icons/bi'
import {GoCheck} from 'react-icons/go'
import AuthenticationService from './services/AuthenticationService';
import UserService from './services/UserService';
import { toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import post_not_found_illustration from "./assets/post-not-found-illustration.svg"

const UserReviewsPage = () => {


    const addPaddingToDateMonthLessThan10 = (n) => {
        return n < 10 ? '0' + n : n
    }
    const [pageCount, setPageCount] = useState(0);
    const [pageSize, setPageSize] = useState(5); // total elements to display per page (DEFAULT)
    const [pageNo, setPageNo] = useState(1); // start at the first page (index of 0 from backend)

    let today = new Date();
    const dd = today.getDate();
    const mm = today.getMonth() + 1; 
    const yyyy = today.getFullYear();
    today = yyyy + '-'+ addPaddingToDateMonthLessThan10(mm) + '-' + dd;

    let sortString = "asc";

    const [review, setReview] = useState({
        review: ""
    })
    const [reviewCount, setReviewCount] = useState(0);

    const [sortBy, setSortBy] = useState("");
    const [userReviewList, setUserReviewList] = useState([]);

    
    const [avatarList, setAvatarList] = useState([]);

    const [errors, setErrors] = useState({
        reviewError: ''
    })

    const [userState, setUserState] = useState({
        fullName: "",
        isLoggedIn: false,
        // isLoggedInText: ""
    });

    const getRequestParams = (pageNo, pageSize, sortBy, sortDir) => {
        let params = {};
    
        // these params must match on the backend (projectName, pageNo, size)
        if (pageNo) params["pageNo"] = pageNo - 1;
        if (pageSize) params["pageSize"] = pageSize;
        if(sortBy) params["sortBy"] = sortBy;
        if(sortDir) params["sortDir"] = sortString;
        
        return params;
      };

      


      const getAllUserReviews = () => {
        const params = getRequestParams(pageNo, pageSize, sortBy);

        let avatars = [];

        UserService.getAllUserReviews(params)
            .then(res => {
                const {totalItems, totalPages, currentPage, reviews} = res.data;
                
                setUserReviewList(reviews)
                setPageCount(totalPages);
                setReviewCount(totalItems)

                avatars.push(reviews.map(m => m.userAvatar ? m.userAvatar.imageUrl: null))
                setAvatarList(...avatars)
            }).catch(err => {
                console.log("err: ", err.response)
               
            })
      }


      const deleteReview = (id) => {

        let reviewList = [...userReviewList];
        UserService.deleteReview(id)
            .then(res => {
                setUserReviewList(reviewList.filter(review => review.id !== id))
                setReviewCount(userReviewList.length -1)
              
            }).catch(err => {
    
                console.log("err: ", err.response)
            })

    }




    useEffect(() => {
        getAllUserReviews();

        const user = AuthenticationService.getCurrentUserJwt();
        const principal = AuthenticationService.getCurrentUserSubject();

        setUserState({
            fullName: user.fullName
        })

        if(user) {
            setUserState({
            isLoggedIn: true,
            fullName: user.fullName
            })
        }
    }, [pageNo, pageSize])


    const handleChange = (event) => {
        const {name, value} = event.target;
        setReview(prev => ({
            ...prev, [name]:value
        }))
    }


    const handleSubmit = (event) => {
        event.preventDefault();
        UserService.createUserReview(review)
            .then(res => {
                setReview({
                    review: ""
                })

                getAllUserReviews();
                toast.success("Review created successfully")
            })
            .catch(err => {
                
                const {data} = err.response;
                if(data.reviewError) {
                    toast.error(data.reviewError)
                }
                setErrors({
                    reviewError: data.review
                })
                console.log(data)
            })

    }
    

    const sortOptions = [
        { value: 'newest', label: 'Date newest - oldest' },
        { value: 'oldest', label: 'Date oldest - latest' }
    ];

    const [sortState, setSortState] = useState({
        selectedOption: null
    })


    return (
        <div className="user-reviews-page">
            <MobileNavigationHeader heading="User reviews" infoDefault="Have some feedback on our service? Feel free to review us below." />
            <div className="content">
                <div className="reviews-container">
          

         
                    <div className="review-count-container">
                        <h5>{reviewCount > 0 ? reviewCount : 0} Reviews</h5>
                        <p className='text-muted'>Based on 1000 users</p>
                        <button type="button" className="btn btn-primary review-modal-btn shadow-none" data-bs-toggle="modal" data-bs-target="#staticBackdrop">Write a review</button>
                    </div>


                    

                    <div className="modal fade write-review-modal-container" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                        <div className="modal-dialog  modal-dialog-centered">
                            <div className="modal-content write-review-modal">
            
                                <div className="modal-header">
                                    <h5 id="staticBackdropLabel">Write a review</h5>
                                
                                    <button type="button" className="btn-close shadow-none" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>

                                <div className="modal-body">
                                    <form className="write-review-modal-form" onSubmit={handleSubmit}>

                                        <div className="form-group">

                                            <div className="mb-3">

                                                <label className="form-label"> Enter your review </label>
                                                
                                                {review.review && review.review.length >= 20 ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.reviewError}</label>}
                                                <textarea type="text" placeholder="Review" className="form-control write-review-text-area shadow-none" name="review" value={review.review} onChange={handleChange} />

                                                <button>Post review</button>

                                            </div>
                                        </div>
                                


                                    </form>
                                    <ToastContainer />
                                </div>

                            </div>
                        </div>
                    </div>


                </div>

    
            </div>

            <hr id="main-divider"/>


            <div className="review-section-container">
            <div className="review-section mt-5">
            

                {sortState.selectedOption && (
                    <>
                        <p>{sortState.selectedOption.value === sortOptions[0].value ? <p>{sortOptions[0].value}</p> : ''}</p>
                        <p>{sortState.selectedOption.value === sortOptions[1].value ? <p>{sortOptions[1].value}</p> : ''}</p>
                    </>

                )}
                    {userReviewList.length > 0 ? userReviewList.map((review, id) => {
                        return (
                            <div className="user-review">
                                <div className="review-header">

                                <img src={avatarList[id] != null ? avatarList[id] : defaultAvatar} alt="user avatar"/>
                   
                                    <div>
                                        <h4>{review.reviewerFullName}</h4>

                                        {AuthenticationService.getCurrentUserSubject() === review.reviewerEmailAddress ?
                                            <button onClick={() => deleteReview(review.id)}>Remove</button> : ""                    
                                        }
                                        
                                    </div>
                               
                                    <button className="review-date-btn">{Math.abs(Math.floor((Date.parse(review.createdAt) - Date.parse(today)) / 86400000))} day(s) ago</button>
                                    
                                </div>
                       
                                <p>{review.review}</p>
                                <hr id="small-divider"/>
                            </div>
                        )
                        
                    }) :     <div className="review-not-found-container mt-5">
                                <h2>No reviews found</h2>
                                <p>Be the first to create a review</p>
                                <img src={post_not_found_illustration}/>
                            </div>
                

                }
              

            
                {userReviewList.length > 0 && (
                    <div className="btn-container mt-5">
                        {/* Load 5 more reviews on each click */}
                        <button className="user-reviews-load-more-btn" onClick={() => setPageSize(pageSize + 5)}><BiPlus size={18} style={{marginRight: ".5rem", fill: "#fff"}} />Load more reviews</button>
                    </div>
                )}




            </div>
            </div>
            <div id="footer-component">
                <FooterComponent />
            </div>
      
            
        </div>
    )
}

export default UserReviewsPage
