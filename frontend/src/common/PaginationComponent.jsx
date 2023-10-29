import React, {useState} from 'react'
import Pagination from "@material-ui/lab/Pagination";
import { red } from '@material-ui/core/colors';
import { makeStyles } from '@material-ui/styles';
import {BiRightArrow, BiLeftArrow} from 'react-icons/bi'

const PaginationComponent = (props) => {

  const useStyles = makeStyles((theme) =>({
    root: {
        '& .Mui-selected': {
          backgroundColor: 'var(--primary-light-blue)',
          color:'#fff',
          fontWeight: "600",
          height: "35px",
          width: "35px",
          border: "none"
         },
         '& .Mui-selected:hover': {
           backgroundColor: "var(--hover-light-blue)",
           border: "none"
         },
         '& ul > li:not(:first-child):not(:last-child) > button:not(.Mui-selected)': {
          // backgroundColor: 'red',
    
          border: "none"
         },
         '& ul > li:not(:first-child):not(:last-child) > button:not(.Mui-selected):hover': {
          // backgroundColor: 'green',
          border: "none",
          

         },
         
    }
  }));


  const classes = useStyles();

    return (
        <div className="pagination-container">
          <div className="content">
            <div className="pagination mt-5">


              <Pagination
                count={props.count}
                page={props.page}
                siblingCount={1}
                boundaryCount={1} 
                variant="text"
                shape="rounded"
                components={{ previous: <BiLeftArrow/>, next:<BiRightArrow/> }}
                onChange={props.onPageChange}
                className={classes.root}
                classes={{selected:classes.selected}}
              />
            </div>
      
          </div>
        </div>
        
    )
}

export default PaginationComponent
