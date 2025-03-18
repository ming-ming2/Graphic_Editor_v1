package command;

import dto.GCommandDTO;

public interface GCommand<T extends GCommandDTO> {
	void execute(T DTO);
}
