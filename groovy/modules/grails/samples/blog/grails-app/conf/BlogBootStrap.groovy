class BlogBootStrap {

     @Property Closure init = { servletContext ->
     	
     	println "Loading Blog data"
     	
		def user = new User(
			firstName:"Joe",
			lastName:"Blogs",
			login:"jblogs",
			password:"me",
			email:"joe.blogs@blogs.com"
		)
				
		def blog = new Blog(name:"Joe's Blog", owner:user)
		user.blog = blog
		
		def entry = new Entry(title:"Test Entry",date:new Date())
		entry.body = "This is a test entry in this demo blog"		
		blog.entries.add(entry)
		
		
		def comment = new Comment()
		comment.authorName = "Fred Flintstone"
		comment.authorEmail = "fred@blogs.com"
		comment.authorBlogURL = "http://www.blogs.com/fred"
		comment.body = "This is my comment!"		
		entry.comments.add(comment)
		
		user.save() 
  
     }
     @Property Closure destroy = {
     }
} 