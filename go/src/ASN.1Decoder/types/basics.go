package types

type Ber struct {
   Raw uint8
}

type ASN struct {
   Class     string
   ClassType string
   DataType  string
   Length    int
   Value     string
   Children  []ASN
}
