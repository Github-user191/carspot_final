import React, { useState } from 'react'
import { Link } from 'react-router-dom'
import postSuccessIllustration from './assets/post-success-illustration.svg'

const PostSuccessModal = () => {


    return (
        <div>


            <div className="modal fade post-created-modal-container" id="postSuccessModal" role="dialog" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content post-created-modal-container">
                        <div className="modal-header">
                            <button type="button" className="btn-close shadow-none" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div className="modal-body">
                            <h3>Congratulations</h3>
                            <p>Your post has been created successfully</p>

                            <img src={postSuccessIllustration} alt="Post success" />

                            <p className='mt-4 mb-3'>It may take a few minutes for your ad to be available</p>

                            <div className="btn-container">
                                <button className='view-ad-btn mt-2'>View Post</button>
                                <Link to="/my-posts">
                                    <button className='view-my-ads-btn mt-2' data-bs-dismiss="modal">View all my Posts</button>
                                </Link>
                                
                            </div>
                        </div>
                    </div>
                </div>
            </div> {/* END MODAL */}

        </div>
    )
}


export default PostSuccessModal