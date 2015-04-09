This folder should contain one or more scenarios (files with json data)
describing an certificate in the external model for the test cases to work. 

-- Scenario concept --


-- Filename structure --
The files should be named with a prefix describing the general intent of the
scenario, a unique scenario name (that can be shared between the different
models if they represent the same certificate) and a file extension.

The prefix should be like:
* valid - for certificates that are valid
* invalid - for certificates with invalid syntax or validation errors.

The extension should be '.json' for the external model.

Example:
* valid-diabetes-1.json
* invalid-validation.json