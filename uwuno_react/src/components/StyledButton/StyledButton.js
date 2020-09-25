import React from "react";
import { Button, makeStyles } from "@material-ui/core";

export const StyledButton = ({
  colour,
  label,
  fullWidth,
  variant = "contained",
}) => {
  const useStyles = makeStyles({
    root: {
      background: colour,
      width: fullWidth ? "100%" : "40%",
    },
    label: {
      textTransform: "lowercase",
      color: "black",
    },
  });

  const classes = useStyles();

  return (
    <Button
      size="large"
      variant={variant}
      classes={{
        root: classes.root,
        label: classes.label,
      }}
    >
      {label}
    </Button>
  );
};
