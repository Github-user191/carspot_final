import React, { useEffect, useState } from "react";
import { Range, getTrackBackground } from "react-range";
import { makeStyles } from "@material-ui/core/styles";
import Slider from '@material-ui/core/Slider';
import Box from '@material-ui/core/Box'



export default function RangeSlider({minValue, maxValue, value, handleChange}) {

  const useStyles = makeStyles({
    root: {

      "&>.MuiSlider-thumb": {
        "&:nth-child(4)": {
          color: "var(--primary-light-blue)"
        },
        "&:nth-child(5)": {
          color: "var(--primary-light-blue)"
        }
      }
    }
  });

  const classes = useStyles();

  return (
    <div className="rangeInput-container">

      <h4 className="price-sub-heading">Between
        <span style={{ fontWeight: "1000", color: "var(--primary-light-blue)", padding: "0 .25rem 0 .25rem" }}>
          R{value[0]}
        </span>
        and
        <span style={{ fontWeight: "1000", color: "var(--primary-light-blue)", paddingLeft: ".25rem" }}>
          R{value[1]}
        </span>
      </h4>

      <Slider value={value} min={minValue} max={maxValue} step={1000} onChange={handleChange} className={classes.root} valueLabelDisplay="off"/>

    </div>

  );
}
