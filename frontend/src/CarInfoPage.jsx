import React, { useState, useEffect } from 'react'
import MobileNavigationHeader from './MobileNavigationHeader'
import car from "./assets/car-post.svg"
import {AiOutlineClockCircle} from 'react-icons/ai'
import {BiPlus} from "react-icons/bi"
import {IoMdEye} from 'react-icons/io'
import {AiOutlineWarning} from 'react-icons/ai'
import UserProfileInfoComponent from './UserProfileInfoComponent'
import {FiPhone} from 'react-icons/fi'
import {BiChat} from 'react-icons/bi'
import {AiOutlineMail} from 'react-icons/ai'
import {BiUser} from 'react-icons/bi'
import defaultAvatar from './assets/user-profile.svg'
import twitter from './assets/twitter.svg'
import facebook from './assets/facebook.svg'
import whatsapp from './assets/whatsapp.svg'
import { Link,useParams} from 'react-router-dom'
import FooterComponent from './common/FooterComponent';
import UserService from './services/UserService'
import PostService from './services/PostService'
import CarItemComponent from './CarItemComponent';
import no_car_preview from "./assets/no-car-preview.jpg"
import "react-responsive-carousel/lib/styles/carousel.min.css"; // requires a loader
import { useNavigate } from 'react-router'
import { Carousel } from 'react-responsive-carousel'
import { BarLoader, CircleLoader, ClipLoader, DotLoader, FadeLoader, GridLoader, HashLoader, MoonLoader, PuffLoader, PulseLoader, RingLoader, SkewLoader } from 'react-spinners';
import { red } from '@material-ui/core/colors'
import Loader from 'react-spinners/DotLoader'
import AuthenticationService from './services/AuthenticationService'
import WatchlistPostService from './services/WatchlistPostService'
import { Bounce, Slide, toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import axios from 'axios'

const CarInfoPage = () => {

    const addPaddingToDateMonthLessThan10 = (n) => {
        return n < 10 ? '0' + n : n
    }

    let today = new Date();
    const dd = today.getDate();
    const mm = today.getMonth() + 1;
    const yyyy = today.getFullYear();
    today = yyyy + '-' + addPaddingToDateMonthLessThan10(mm) + '-' + dd;

    const [loading, setLoading] = useState(false);
    const [postCreatorInfo, setPostCreatorInfo] = useState({
        postCreatorFullName: "",
        postCreatorEmailAddress: "",
        postCreatorMobileNumber: ""

    });
    const [avatar, setAvatar] = useState(null);

    const [userPosts, setUserPosts] = useState([]);
    const [postViewCount, setPostViewCount] = useState(0)
    const [principal, setPrincipal] = useState();
    const [postInfo, setPostInfo] = useState({
        title:"",
        description:"",
        brand:"",
        model:"",
        price:"",
        color:"",
        kilometers:"",
        bodyType:"",
        fuelType:"",
        transmission:"",
        year:"",
        province:"",
        city: "",
        active: "",
        postImages: [],
        postCreatorName: "",
        postCreatorEmail: ""
    });

    const {id} = useParams();

    useEffect(() => {
        setLoading(true)

        
        PostService.getPostViewsByPostId(id)
        .then(res => {
            setPostViewCount(res.data)
        }).catch(err => {
            setPostViewCount(0)
        })


        PostService.getPostById(id)
            .then(res => {

                const {postCreatorEmail, active, postImages} = res.data;
                if(!active) {
                    toast.error("This post has expired. It will not be visible to other users")
                }
                setPostInfo(res.data)
                setPrincipal(AuthenticationService.getCurrentUserSubject())

                setLoading(true)

                UserService.getUserInfoByEmailAddress(postCreatorEmail)
                .then(res => {
                    const {fullName, emailAddress, userAvatar, mobileNumber} = res.data;
                    setPostCreatorInfo({
                        postCreatorFullName: fullName,
                        postCreatorEmailAddress: emailAddress,
                        postCreatorMobileNumber: mobileNumber
                    });

                    if(userAvatar) {
                        setAvatar(userAvatar.imageUrl)
                    }
 
                }).catch(err => {
                    toast.error("There was an error performing this action. Please try again shortly")
                });

                PostService.getUserPostsByEmailAddress(postCreatorEmail)
                .then(res => {
                    let userPostsList = [];
                    if(res.data != null) {
                        for(let i = 0; i <= res.data.posts.length - 1; i++) {
                            userPostsList.push(res.data.posts[i])
                            
                        }
    
                        setUserPosts(userPostsList)
                    } 
                }).catch(err => {
                    toast.error("There was an error performing this action. Please try again shortly")
                })

        
                setLoading(false)
    
            }).catch(err => {
                toast.error("There was an error performing this action. Please try again shortly")
            })

    }, [])



    
  const addWatchlistPost = () => {
    WatchlistPostService.addWatchlistPost(id)
    .then(res => {
      toast.success(res.data)
    }).catch(err => {
      toast.error(err.response.data.postNotFound)
    });

  }
    

    return (


        <div>

            <MobileNavigationHeader/>
            <div className="car-info-page-container">

                <div className="car-info-page-right-content">

                    {userPosts.length > 0 ? <h4>Some of {postCreatorInfo.postCreatorFullName}'s ads</h4> : ""}
                    {userPosts && (
                        userPosts.map(post => {
                            return (
                                <>

                            
                                    <Link to={`/car-info/${post.id}`} style={{ textDecoration: "none" }} onClick={window.location.reload}>
                                        {
                                            post.postImages[0] ?
                                           
                                            <CarItemComponent
                                                imageUrl={post.postImages[0].imageUrl}
                                                key={post.id}
                                                time={post.createdAt}
                                                title={post.title}
                                                price={post.price}
                                                description={post.description}
                                                location={post.city}
                                            />

                                            :

                                            <CarItemComponent
                                                noPreviewImage={no_car_preview}
                                                key={post.id}
                                                time={post.createdAt}
                                                title={post.title}
                                                price={post.price}
                                                description={post.description}
                                                location={post.city}
                                            />
                                        }
                                    </Link>
                                </>
                            )
                        
                    }))}
                  


                </div>


                <div className='car-info-page'>
                <ToastContainer style={{marginTop: "3rem"}} />

                    <div className="car-main-container">

                        <div className="header">
                            <h4>{postInfo.title}  </h4>
                      
                            <h2>R{postInfo.price}</h2>
                            
                        </div>
                        <Carousel onSwipeMove={true} showThumbs={false} className="carousel">

                            {!loading ? (
                                !postInfo.postImages[0] ? (
                                    <div className='image-container'>
                                        <img src={no_car_preview} alt="car image" />
                                    </div>
                                ) :
                                    postInfo.postImages.map(image => {
                                        return (
                                            <div className='image-container'>

                                                <img src={image.imageUrl} alt="car image" />
                                            </div>
                                        )
                                    })
                            ) :

                                <div className="sweet-loading">
                                    <PulseLoader color='gray' size={20} speedMultiplier={0.8} />
                                </div>
                            }
                              
  
                            </Carousel>
                  

                        <div className="metadata">
                            <div className="data-one">
                                <button type="button" className='time-btn'>
                                    <AiOutlineClockCircle className="icon" size={15} />
                                    {Math.abs(Math.floor((Date.parse(postInfo.createdAt) - Date.parse(today)) / 86400000))} day(s) ago
                                </button>
                                <button type="button" className='watchlist-btn' onClick={addWatchlistPost}>
                                    <BiPlus className="icon" size={15}/> Watchlist
                                </button>
                                <button type="button" className='views-btn'>
                                    <IoMdEye className="icon" size={15}/>{postViewCount} view(s)
                                </button>
                                <button type="button" className='report-btn'>
                                    <AiOutlineWarning className="icon" size={15}/> Report
                                </button>
                            </div>


                        </div>

                        <div className="vehicle-information-container">

                        <hr id="main-divider"/>

                            <div className="vehicle-information">
                                <h4>Vehicle Information </h4>
                                <ul className='mt-4'>
                                    <li><span className="list-heading"> Location: </span> {postInfo.province}, {postInfo.city} </li>
                                    <li><span className="list-heading"> For sale by: </span> {postInfo.postCreatorName}</li>
                                    <li><span className="list-heading"> Brand: </span> {postInfo.brand}</li>
                                    <li><span className="list-heading"> Model: </span> {postInfo.model}</li>
                                    <li><span className="list-heading"> Body type: </span> {postInfo.bodyType}</li>
                                    <li><span className="list-heading"> Fuel type: </span> {postInfo.fuelType}</li>
                                    <li><span className="list-heading"> Kilometers: </span> {postInfo.kilometers}</li>
                                    <li><span className="list-heading"> Transmission: </span> {postInfo.transmission}</li>
                                    <li><span className="list-heading"> Color: </span> {postInfo.color}</li>
                                </ul>
                            </div>

                            <hr id="main-divider"/>
                        
                            <div className="description-information">
                                <h4>Description </h4>
                                <p className='mt-4'> 


                                {postInfo.description}
                                </p>
                            </div>

                            <hr id="main-divider"/>

                            <div className='seller-information'>
                                <h4 className='mb-5'>Seller info </h4>


                                <UserProfileInfoComponent
                                    fullName={postCreatorInfo.postCreatorFullName}
                             
                                    defaultAvatar={!avatar && defaultAvatar}
                                    avatar={avatar && avatar}
                                    yearJoined={2019}
                                    phoneIcon={<FiPhone size={16}/>}
                                    phoneText="Call"
                                    phoneInfo={postCreatorInfo.postCreatorMobileNumber}
                                    emailIcon={<AiOutlineMail size={16} />}
                                    emailText="Email"
                                    emailInfo={postCreatorInfo.postCreatorEmailAddress}
                                    chatIcon={<BiChat size={16}/>}
                                    chatText="Chat"
                                    chatInfo={`Chat with ${postCreatorInfo.postCreatorFullName.split(" ")[0]}`}
                                />
                            </div>

                            <hr id="main-divider"/>

                                
                
                        </div>



                        <div className="ad-share-edit-container">


                            {AuthenticationService.getCurrentUserSubject() === postInfo.postCreatorEmail ? 
                                <Link to={`/edit-ad/${id}`}>
                                    <button className="edit-ad-btn">Edit</button>
                                </Link>

                                : ""
                            }

                    

                            <p className='share-ad-label mt-4'>Share this ad</p>

                            <div className="socials">
                                <div className="twitter">
                                    <img src={twitter} alt="twitter logo" />
                                </div>
                                <div className="facebook">
                                    <img src={facebook} alt="facebook logo" />
                                </div>
                                <div className="whatsapp">
                                    <img src={whatsapp} alt="whatsapp logo" />
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

export default CarInfoPage
