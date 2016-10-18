package types

import (
   "fmt"
   "time"
   //"errors"
   "runtime"
   //"regexp"
)

type CustomError struct {
   What string
}

type Abort struct {
   What error
}

type NoDataFound struct {   
}

func (e *CustomError) Error() string {
   return fmt.Sprintf("%v Error at %s: %s\n", time.Now(), GetBackTrace(), e.What)
}

func (e *Abort) Error() string {
   return fmt.Sprintf("%v Fatal error: %s\n", time.Now(), e.What.Error())
}

func (e *NoDataFound) Error() string {
   return fmt.Sprintf("%v Error: No data found\n", time.Now())
}

func GetBackTrace() string {
   // Skip this function, and fetch the PC and file for its parent
   pc, _, _, _ := runtime.Caller(1)

   // Retrieve a Function object this functions parent
   functionObject := runtime.FuncForPC(pc)

   // Regex to extract just the function name (and not the module path)
   //extractFnName := regexp.MustCompile(`^.*\.(.*)$`)

   //return extractFnName.ReplaceAllString(functionObject.Name(), "$1")
   return functionObject.Name()
}