import React from 'react';
import TextField from '@material-ui/core/TextField';
import { makeStyles } from '@material-ui/core/styles';
// import { ButtonIncreDecrement as Button } from "../StyledButton/ButtonIncreDecrement";

export const StyledTextField = ({
  background,
  textColour,
  label,
  variant = "outlined",
  fullWidth,
}) => {
  const useStyles = makeStyles({
    root: {
      textTransform: "lowercase",
      color: textColour,
      fontSize: "1rem",
    },
    textField: {
      padding: "0px",
      width: fullWidth ? "100%" : "40%",
      backgroundColor: background,
    },
  });

  const classes = useStyles();
  
  return (
    <form className={classes.root} noValidate autoComplete="off">
      <div>
        <div>{label}</div>
        <TextField
          id="number"
          type="number"
          InputProps={{
            // endAdornment: <Button
            //                 colour="#FF69B4"
            //                 groupColour="secondary"
            //               />,
            className: classes.textField,
          }}
          variant={variant}
        />
      </div>
    </form>
  );
};