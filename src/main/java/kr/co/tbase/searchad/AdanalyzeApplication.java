package kr.co.tbase.searchad;

import kr.co.tbase.searchad.entity.Members;
import kr.co.tbase.searchad.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCrypt;

@SpringBootApplication
public class AdanalyzeApplication  implements CommandLineRunner{//
	private final MemberRepository memberRepository;

	public AdanalyzeApplication(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public static void main(String[] args) {

		SpringApplication.run(AdanalyzeApplication.class, args);
	}

	@Override
	public void run(String... args) {


		Members admin = new Members(1,"admin", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()),"관리자", "admin");
		memberRepository.save(admin);

		admin = new Members(2,"a", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()),"amy", "member");
		memberRepository.save(admin);
		admin = new Members(3,"aa", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()),"bear", "member");
		memberRepository.save(admin);
		admin = new Members(4,"cccc", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()),"elephant", "member");
		memberRepository.save(admin);
		admin = new Members(5,"bb", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()),"myx", "member");
		memberRepository.save(admin);
		admin = new Members(6,"aaaaa", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(7,"abc", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(8,"abbc", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(9,"a3dmin", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin= new Members(10,"asafn", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(11,"any", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(12,"jyo", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(13,"sisi", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(14, "ubrb", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()), "멤버", "member");
		memberRepository.save(admin);
		admin = new Members(15,"aasffaaa", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(16,"aadfbc", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(17,"akkbbc", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(18,"a3dbgmin", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin= new Members(19,"asammfn", BCrypt.hashpw("1234qwer!", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);

	}


}


