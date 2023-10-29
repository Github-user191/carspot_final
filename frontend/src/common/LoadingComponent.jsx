import React, {useState} from 'react'
import { PulseLoader } from 'react-spinners';

const LoadingComponent = ({color}) => {
    return (
        <div className="sweet-loading">
            <PulseLoader color={color} size={8} />
      </div>
    )
}

export default LoadingComponent
