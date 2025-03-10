package com.yaksha.assignment.functional;

import static com.yaksha.assignment.utils.MasterData.getBook;
import static com.yaksha.assignment.utils.MasterData.getBookList;
import static com.yaksha.assignment.utils.MasterData.getBookPage;
import static com.yaksha.assignment.utils.TestUtils.businessTestFile;
import static com.yaksha.assignment.utils.TestUtils.currentTest;
import static com.yaksha.assignment.utils.TestUtils.testReport;
import static com.yaksha.assignment.utils.TestUtils.yakshaAssert;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.yaksha.assignment.controller.BookController;
import com.yaksha.assignment.entity.Book;
import com.yaksha.assignment.service.BookService;
import com.yaksha.assignment.utils.JavaParserUtils;
import com.yaksha.assignment.utils.MasterData;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc
public class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService;

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	public void testGetAllBooks() throws Exception {
		List<Book> books = getBookList(); // Get a list of books
		when(bookService.getAllBooks(any())).thenReturn(getBookPage(0, 1)); // Get paginated page (page 0, size 1)

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/books?page=0&size=1")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String responseContent = result.getResponse().getContentAsString();

		yakshaAssert(currentTest(), (responseContent != null && !responseContent.isEmpty() ? "true" : "false"),
				businessTestFile);
	}

	@Test
	public void testGetBookById() throws Exception {
		Book book = getBook();
		when(bookService.getBookById(eq(book.getId()))).thenReturn(book);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/books/" + book.getId())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(),
				(result.getResponse().getContentAsString().contentEquals(MasterData.asJsonString(book)) ? "true"
						: "false"),
				businessTestFile);
	}

	@Test
	public void testDeleteBook() throws Exception {
		Book book = getBook();
		when(bookService.getBookById(eq(book.getId()))).thenReturn(book);
		when(bookService.getAllBooks(any())).thenReturn(getBookPage(0, 1));

		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/books/" + book.getId())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(), (result.getResponse().getContentAsString().contentEquals("") ? "true" : "false"),
				businessTestFile);
	}

	@Test
	public void testGetBooksByTitle() throws Exception {
		String title = "Test Title";
		List<Book> books = getBookList();
		when(bookService.getBooksByTitle(eq(title), any())).thenReturn(getBookPage(0, 1));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/books/search/title/" + title + "?page=0&size=1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String responseContent = result.getResponse().getContentAsString();
		yakshaAssert(currentTest(), (responseContent != null && !responseContent.isEmpty() ? "true" : "false"),
				businessTestFile);
	}

	@Test
	public void testGetBooksByRatingAbove() throws Exception {
		double rating = 4.0;
		List<Book> books = getBookList();
		when(bookService.getBooksByRatingAbove(eq(rating), any())).thenReturn(getBookPage(0, 1));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/books/search/rating/" + rating + "?page=0&size=1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String responseContent = result.getResponse().getContentAsString();
		yakshaAssert(currentTest(), (responseContent != null && !responseContent.isEmpty() ? "true" : "false"),
				businessTestFile);
	}

	@Test
	public void testGetBooksByAuthor() throws Exception {
		String author = "Test Author";
		List<Book> books = getBookList();
		when(bookService.getBooksByAuthor(eq(author), any())).thenReturn(getBookPage(0, 1));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/books/search/author/" + author + "?page=0&size=1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String responseContent = result.getResponse().getContentAsString();

		yakshaAssert(currentTest(), (responseContent != null && !responseContent.isEmpty() ? "true" : "false"),
				businessTestFile);
	}

	@Test
	public void testControllerAnnotations() throws IOException {
		boolean hasRestControllerAnnotation = JavaParserUtils.checkClassAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "RestController");
		System.out.println("Controller has @RestController annotation: " + hasRestControllerAnnotation);

		boolean hasRequestMappingAnnotation = JavaParserUtils.checkClassAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "RequestMapping");
		System.out.println("Controller has @RequestMapping annotation: " + hasRequestMappingAnnotation);

		// Using yakshaAssert at the end to verify all conditions
		yakshaAssert(currentTest(), (hasRestControllerAnnotation && hasRequestMappingAnnotation ? "true" : "false"),
				businessTestFile);
	}

	@Test
	public void testMethodAnnotations() throws IOException {
		boolean hasPostMappingAnnotation = JavaParserUtils.checkMethodAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "createBook", "PostMapping");
		System.out.println("createBook method has @PostMapping annotation: " + hasPostMappingAnnotation);

		boolean hasGetMappingAnnotation = JavaParserUtils.checkMethodAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooks", "GetMapping");
		System.out.println("getBooks method has @GetMapping annotation: " + hasGetMappingAnnotation);

		boolean hasGetMappingByIdAnnotation = JavaParserUtils.checkMethodAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBookById", "GetMapping");
		System.out.println("getBookById method has @GetMapping annotation: " + hasGetMappingByIdAnnotation);

		boolean hasGetMappingByTitleAnnotation = JavaParserUtils.checkMethodAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooksByTitle", "GetMapping");
		System.out.println("getBooksByTitle method has @GetMapping annotation: " + hasGetMappingByTitleAnnotation);

		boolean hasGetMappingByRatingAboveAnnotation = JavaParserUtils.checkMethodAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooksByRatingAbove",
				"GetMapping");
		System.out.println(
				"getBooksByRatingAbove method has @GetMapping annotation: " + hasGetMappingByRatingAboveAnnotation);

		boolean hasGetMappingByAuthorAnnotation = JavaParserUtils.checkMethodAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooksByAuthor", "GetMapping");
		System.out.println("getBooksByAuthor method has @GetMapping annotation: " + hasGetMappingByAuthorAnnotation);

		// Using yakshaAssert at the end to verify all conditions
		yakshaAssert(currentTest(),
				(hasPostMappingAnnotation && hasGetMappingAnnotation && hasGetMappingByIdAnnotation
						&& hasGetMappingByTitleAnnotation && hasGetMappingByRatingAboveAnnotation
						&& hasGetMappingByAuthorAnnotation ? "true" : "false"),
				businessTestFile);
	}

	@Test
	public void testMethodParameterAnnotations() throws IOException {
		boolean hasRequestBodyAnnotationOnCreateBook = JavaParserUtils.checkMethodParameterAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "createBook", "book",
				"RequestBody");
		System.out.println(
				"createBook method has @RequestBody on 'book' parameter: " + hasRequestBodyAnnotationOnCreateBook);

		boolean hasPathVariableAnnotationOnGetBookById = JavaParserUtils.checkMethodParameterAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBookById", "id",
				"PathVariable");
		System.out.println(
				"getBookById method has @PathVariable on 'id' parameter: " + hasPathVariableAnnotationOnGetBookById);

		boolean hasRequestParamOnGetBooksPage = JavaParserUtils.checkMethodParameterAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooks", "page",
				"RequestParam");
		System.out.println("getBooks method has @RequestParam on 'page' parameter: " + hasRequestParamOnGetBooksPage);

		boolean hasRequestParamOnGetBooksSize = JavaParserUtils.checkMethodParameterAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooks", "size",
				"RequestParam");
		System.out.println("getBooks method has @RequestParam on 'size' parameter: " + hasRequestParamOnGetBooksSize);

		// Using yakshaAssert at the end to verify all conditions
		yakshaAssert(currentTest(),
				(hasRequestBodyAnnotationOnCreateBook && hasPathVariableAnnotationOnGetBookById
						&& hasRequestParamOnGetBooksPage && hasRequestParamOnGetBooksSize ? "true" : "false"),
				businessTestFile);
	}

	@Test
	public void testMethodReturnTypes() throws IOException {
		boolean isCreateBookReturnTypeCorrect = JavaParserUtils.checkMethodReturnType(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "createBook",
				"ResponseEntity<Book>");
		System.out.println("createBook method return type is ResponseEntity<Book>: " + isCreateBookReturnTypeCorrect);

		boolean isGetBooksReturnTypeCorrect = JavaParserUtils.checkMethodReturnType(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooks",
				"ResponseEntity<Page<Book>>");
		System.out.println("getBooks method return type is ResponseEntity<Page<Book>>: " + isGetBooksReturnTypeCorrect);

		boolean isGetBookByIdReturnTypeCorrect = JavaParserUtils.checkMethodReturnType(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBookById",
				"ResponseEntity<Book>");
		System.out.println("getBookById method return type is ResponseEntity<Book>: " + isGetBookByIdReturnTypeCorrect);

		boolean isGetBooksByTitleReturnTypeCorrect = JavaParserUtils.checkMethodReturnType(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooksByTitle",
				"ResponseEntity<Page<Book>>");
		System.out.println("getBooksByTitle method return type is ResponseEntity<Page<Book>>: "
				+ isGetBooksByTitleReturnTypeCorrect);

		boolean isGetBooksByRatingAboveReturnTypeCorrect = JavaParserUtils.checkMethodReturnType(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooksByRatingAbove",
				"ResponseEntity<Page<Book>>");
		System.out.println("getBooksByRatingAbove method return type is ResponseEntity<Page<Book>>: "
				+ isGetBooksByRatingAboveReturnTypeCorrect);

		boolean isGetBooksByAuthorReturnTypeCorrect = JavaParserUtils.checkMethodReturnType(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooksByAuthor",
				"ResponseEntity<Page<Book>>");
		System.out.println("getBooksByAuthor method return type is ResponseEntity<Page<Book>>: "
				+ isGetBooksByAuthorReturnTypeCorrect);

		// Using yakshaAssert at the end to verify all conditions
		yakshaAssert(currentTest(),
				(isCreateBookReturnTypeCorrect && isGetBooksReturnTypeCorrect && isGetBookByIdReturnTypeCorrect
						&& isGetBooksByTitleReturnTypeCorrect && isGetBooksByRatingAboveReturnTypeCorrect
						&& isGetBooksByAuthorReturnTypeCorrect ? "true" : "false"),
				businessTestFile);
	}

}
