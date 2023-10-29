import React from 'react'
import SkeletonElement from './SkeletonElement'


const SkeletonCarCard = () => {
    return (
        <div className="wrapper">
            <div className="car-card-skeleton">

                <div className='left'>
                    <SkeletonElement type="image" />
                </div>
           
            {/* <div className="image-container"></div> */}
                <div className='right'>
                    
                    <SkeletonElement type="title-text" />
                    <SkeletonElement type="price-text" />
                    <SkeletonElement type="description-text" />
                    <SkeletonElement type="metadata-text" />
                </div>

            </div>
    </div>
    )
}

export default SkeletonCarCard
