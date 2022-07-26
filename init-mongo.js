db.createUser(
  {
    user  : "quake",
    pwd   : "quake",
    roles : [
      {
        role  : "readWrite",
        db    : "quakedb"
      }
    ]
  }
)